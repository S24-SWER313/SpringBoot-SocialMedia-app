package com.project.proo.postInfo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project.proo.usreInfo.UserController;

@Component
public class CommentModelAssembler implements RepresentationModelAssembler<Comment, EntityModel<Comment>> {

    @Override
    public EntityModel<Comment> toModel(Comment comment) {
        return EntityModel.of(comment,
                // Link to Comment
                linkTo(methodOn(CommentController.class).getComment(comment.getPost().getId(),comment.getId())).withSelfRel(),
                // Link to related Post
                linkTo(methodOn(PostController.class).one(comment.getPost().getUser().getId(),comment.getPost().getId())).withRel("posts"),
                 linkTo(methodOn(UserController.class).getUser(comment.getUser().getId())).withRel("user"),

                 linkTo(methodOn(LikeController.class).getAllLikesForComment(comment.getPost().getId(),comment.getId())).withRel("likes")
        );
    }
}
