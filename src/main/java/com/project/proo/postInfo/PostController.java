package com.project.proo.postInfo;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.proo.profileInfo.ProfileNotFoundException;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userid}")
public class PostController {
    
  private final PostRepository postRepository;
  private final PostModelAssembler assembler;
  private final UserRepository userRepository;

public PostController(PostRepository postRepository,PostModelAssembler assembler, UserRepository userRepository) {
       this.postRepository = postRepository;
       this.assembler=assembler;
       this.userRepository=userRepository;
}

@GetMapping("/posts/{postid}")
@Transactional
public EntityModel<Post> one(@PathVariable("postid") Integer postid) {

    Post post = postRepository.findById(postid) //
        .orElseThrow(() -> new PostNotFoundException(postid));
  
    return assembler.toModel(post);
}

@GetMapping("/posts")
public CollectionModel<EntityModel<Post>> all(@PathVariable("userid") Integer userId) {
    List<EntityModel<Post>> posts = postRepository.findByUserId(userId).stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(posts, linkTo(methodOn(PostController.class).all(userId)).withSelfRel());
}

@PostMapping("/posts")
ResponseEntity<?> addPofile(@RequestBody Post newPost) {

    EntityModel<Post> entityModel = assembler.toModel(postRepository.save(newPost));

    return ResponseEntity //
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
}


@PutMapping("/posts/{postId}") // Specify the postId in the mapping
public ResponseEntity<?> editPost(@PathVariable Integer postId, @RequestBody Post updatedPost) {
    Post post = postRepository.findById(postId) // Find post by postId, not userId
            .orElseThrow(() -> new PostNotFoundException(postId));

    post.setCaption(updatedPost.getCaption());
    post.setAudiance(updatedPost.getAudiance()); 

    Post savedPost = postRepository.save(post);
    EntityModel<Post> entityModel = assembler.toModel(savedPost);

    return ResponseEntity.ok(entityModel);
}

}
