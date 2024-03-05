package com.project.proo.usreInfo;


import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
  @Autowired 
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


    
}
