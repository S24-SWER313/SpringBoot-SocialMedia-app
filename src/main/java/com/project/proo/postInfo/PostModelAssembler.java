package com.project.proo.postInfo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {

    @Override
  public EntityModel<Post> toModel(Post post) {
   Integer userId = post.getUser().getId();

    return EntityModel.of(post, //
        linkTo(methodOn(PostController.class).one(userId,post.getId())).withSelfRel(),
        linkTo(methodOn(PostController.class).all(userId)).withRel("Posts"));
  }
 
    
}
