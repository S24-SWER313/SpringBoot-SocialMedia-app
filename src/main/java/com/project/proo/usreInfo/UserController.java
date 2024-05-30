package com.project.proo.usreInfo;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.proo.postInfo.PostModelAssembler;
import com.project.proo.postInfo.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Optional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
@RestController
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler assembler;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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
    // List<EntityModel<User>> users = userRepository.findAll().stream()
    // .map(assembler::toModel)
    // .collect(Collectors.toList());

    // return CollectionModel.of(users,
    // linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
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

    // ResponseEntity<?> newUser(@RequestBody User newUser) {

    // EntityModel<User> entityModel =
    // assembler.toModel(userRepository.save(newUser));

    // return ResponseEntity //
    // .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
    // .body(entityModel);
    // }

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

    ///// need to be checked
   @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId, @Valid @RequestBody User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Check if the new username is already taken by another user
        Optional<User> existingUser = userRepository.findByUsername(updatedUser.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // Check if the new email is already taken by another user
        Optional<User> existingUserEmail = userRepository.findByEmail(updatedUser.getEmail());
        if (existingUserEmail.isPresent() && !existingUserEmail.get().getId().equals(userId)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,40}$";
        if (!updatedUser.getPassword().matches(passwordPattern)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Password must be strong");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Update user information
        user.setUsername(updatedUser.getUsername());
        String encodedPassword = encoder.encode(updatedUser.getPassword());
        user.setPassword(encodedPassword);
        user.setEmail(updatedUser.getEmail());

        User savedUser = userRepository.save(user);

        EntityModel<User> entityModel = assembler.toModel(savedUser);
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

    @GetMapping("/users/{userId}/recommended-friends")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getRecommendedFriends(@PathVariable Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Get all users who are not already friends with the user
        List<User> recommendedFriends = userRepository.findAll().stream()
                .filter(u -> !user.getFriends().contains(u) && !u.equals(user))
                .collect(Collectors.toList());

        List<EntityModel<User>> recommendedFriendsModels = recommendedFriends.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<User>> collectionModel = CollectionModel.of(recommendedFriendsModels,
                linkTo(methodOn(UserController.class).getRecommendedFriends(userId)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

}