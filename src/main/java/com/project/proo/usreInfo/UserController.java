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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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

@GetMapping("users/{userId}/friends")
public CollectionModel<EntityModel<User>> getUserFriends(@PathVariable Integer userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    List<EntityModel<User>> friends = user.getFriends().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(friends);
}
@PostMapping("/users")
 //User createUser(@RequestBody User newUser) {
    
  //  User savedUser = userRepository.save(newUser);
    //return userRepository.save(newUser);
    // EntityModel<User> userEntityModel = assembler.toModel(savedUser);
    // return ResponseEntity.created(userEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
    //                      .body(userEntityModel);
    ResponseEntity<?> newUser(@RequestBody User newUser) {

		EntityModel<User> entityModel = assembler.toModel(userRepository.save(newUser));

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}



@PostMapping("/users/{userId}/friends")
public ResponseEntity<?> addFriendsToUser(@PathVariable Integer userId, @RequestParam List<Integer> friendIds) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

    // Assuming you have a method to find users by their IDs
    List<User> friendsToAdd = userRepository.findAllById(friendIds);

    // Manually add each friend to the user's friend list
    friendsToAdd.forEach(friend -> {
        if (!user.getFriends().contains(friend)) {
            user.getFriends().add(friend);
        }
    });

    // Save the updated user
    userRepository.save(user);

    return ResponseEntity.ok().build();
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





    
}
