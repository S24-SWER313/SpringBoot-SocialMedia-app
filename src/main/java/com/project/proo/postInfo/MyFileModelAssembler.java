package com.project.proo.postInfo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class MyFileModelAssembler implements RepresentationModelAssembler<MyFile, EntityModel<MyFile>> {

    @Override
    public EntityModel<MyFile> toModel(MyFile file) {
        Integer fileId = file.getId(); // Assuming file id is directly accessible from MyFile
        Integer postId = file.getPost().getId();
        Integer userId = file.getPost().getUser().getId();

        return EntityModel.of(file, //
                linkTo(methodOn(MyFileController.class).one(postId, fileId)).withSelfRel(),
                linkTo(methodOn(PostController.class).one(userId, postId)).withRel("Post"));
    }
}


