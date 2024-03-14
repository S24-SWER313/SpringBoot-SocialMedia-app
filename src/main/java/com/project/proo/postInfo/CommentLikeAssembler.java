package com.project.proo.postInfo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project.proo.usreInfo.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CommentLikeAssembler implements RepresentationModelAssembler<CommentLike, EntityModel<CommentLike>> {

    @Override
    public EntityModel<CommentLike> toModel(CommentLike commentLike) {
        return EntityModel.of(commentLike,
                
                linkTo(methodOn(LikeController.class).getCommentLike(commentLike.getComment().getPost().getId(),commentLike.getComment().getId(),commentLike.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getUser(commentLike.getUser().getId())).withRel("user")
        );
    }
}

