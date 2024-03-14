package com.project.proo.postInfo;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userid}")
public class PostController {
    
  private final PostRepository postRepository;
  private final PostModelAssembler assembler;

public PostController(PostRepository postRepository,PostModelAssembler assembler) {
    this.postRepository = postRepository;
      this.assembler=assembler;
}

@GetMapping("/posts/{postid}")
@Transactional
public
EntityModel<Post> one(@PathVariable("postid") Integer postid) {

    Post post = postRepository.findById(postid) //
        .orElseThrow(() -> new PostNotFoundException(postid));
  
    return assembler.toModel(post);
}

 /*  @GetMapping("/posts")
public
CollectionModel<EntityModel<Post>> all() {

  List<EntityModel<Post>> post = postRepository.findAll().stream() //
      .map(assembler::toModel) //
      .collect(Collectors.toList());

  return CollectionModel.of(post, linkTo(methodOn(PostController.class).all()).withSelfRel());
}*/

@GetMapping("/posts")
public CollectionModel<EntityModel<Post>> all(@PathVariable("userid") Integer userId) {
    List<EntityModel<Post>> posts = postRepository.findByUserId(userId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(posts, linkTo(methodOn(PostController.class).all(userId)).withSelfRel());
}

  
    
}
