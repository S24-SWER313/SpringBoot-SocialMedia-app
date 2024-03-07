package com.project.proo.postInfo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project.proo.usreInfo.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostLikeAssembler implements RepresentationModelAssembler<PostLike, EntityModel<PostLike>> {

    @Override
    public EntityModel<PostLike> toModel(PostLike postLike) {
        return EntityModel.of(postLike,
                // Link to PostLike
                linkTo(methodOn(LikeController.class).getPostLike(postLike.getPost().getId(),postLike.getId())).withSelfRel(),
             linkTo(methodOn(UserController.class).getUser(postLike.getUser().getId())).withRel("user")
                // Add more links as needed
        );
    }
}
