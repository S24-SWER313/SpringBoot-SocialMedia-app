package com.project.proo.usreInfo;


import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.proo.postInfo.PostModelAssembler;
import com.project.proo.postInfo.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

     
  private final UserRepository userRepository;
  private final UserModelAssembler assembler;


public UserController(UserRepository userRepository, UserModelAssembler assembler) {
    this.userRepository = userRepository;
    this.assembler = assembler;

}

@GetMapping("users/{userId}")
@Transactional
public EntityModel<User> getUser(@PathVariable Integer userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    return assembler.toModel(user);
}

// @GetMapping("/users")
// public CollectionModel<EntityModel<User>> getAllUsers() {
//     List<EntityModel<User>> users = userRepository.findAll().stream()
//             .map(assembler::toModel)
//             .collect(Collectors.toList());

//     return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
// }

@GetMapping("/users")
public CollectionModel<EntityModel<User>> getAllUsers() {
    List<EntityModel<User>> users = userRepository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
            }
        }

    return null;
}




@GetMapping("/users/{userId}/friends")
public ResponseEntity<CollectionModel<EntityModel<User>>> getUserFriends(@PathVariable Integer userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    List<EntityModel<User>> friends = user.getFriends().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(friends,
            linkTo(methodOn(UserController.class).getUserFriends(userId)).withSelfRel());

    return ResponseEntity.ok(collectionModel);
}


// @PostMapping("/users")

//     ResponseEntity<?> newUser(@RequestBody User newUser) {

// 		EntityModel<User> entityModel = assembler.toModel(userRepository.save(newUser));

// 		return ResponseEntity //
// 				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
// 				.body(entityModel);
// 	}



@PostMapping("/users/{userId}/friends")
public ResponseEntity<?> addFriendsToUser(@PathVariable Integer userId, @RequestParam Integer friendIds) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    User friend = userRepository.findById(friendIds)
    .orElseThrow(() -> new UserNotFoundException(userId));

    if (!user.getFriends().contains(friend)) {
        user.getFriends().add(friend);
        friend.getFriends().add(user);
}
   // userRepository.save(user);
    EntityModel<User> entityModel = assembler.toModel(userRepository.save(user));
    return ResponseEntity //
     				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
     				.body(entityModel);
   // return ResponseEntity.ok().build();
}

/////need to be checked 
@PutMapping("/users/{userId}")
public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody User updatedUser) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    // Update user information
    user.setUsername(updatedUser.getUsername());
    user.setPassword(updatedUser.getPassword());
    user.setEmail(updatedUser.getEmail());

    // Save the updated user
    User savedUser = userRepository.save(user);

    // Convert the saved user to an EntityModel
    EntityModel<User> entityModel = assembler.toModel(savedUser);

    // Return response with the updated user
    return ResponseEntity.ok(entityModel);
}

@DeleteMapping("/users/{userId}/friends/{friendId}")
public ResponseEntity<?> deleteFriendship(@PathVariable Integer userId, @PathVariable Integer friendId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new UserNotFoundException(friendId));

    if (user.getFriends().contains(friend)) {
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        userRepository.save(user);
        userRepository.save(friend);
        return ResponseEntity.noContent().build();
    } else {
        // Friendship not found, return an error response
        return ResponseEntity.notFound().build();
    }
}




    
}
