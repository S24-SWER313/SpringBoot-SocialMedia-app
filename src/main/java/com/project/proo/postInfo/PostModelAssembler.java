package com.project.proo.postInfo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project.proo.usreInfo.UserController;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {

    @Override
  public EntityModel<Post> toModel(Post post) {
   Integer userId = post.getUser().getId();

    return EntityModel.of(post, //
        linkTo(methodOn(PostController.class).one(userId,post.getId())).withSelfRel(),
        linkTo(methodOn(PostController.class).all(userId)).withRel("Posts"),
        linkTo(methodOn(UserController.class).getUser(post.getUser().getId())).withRel("user"));


  }
 
    
}