// package com.project.proo.profileInfo;

// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

// import org.springframework.hateoas.EntityModel;
// import org.springframework.hateoas.server.RepresentationModelAssembler;
// import org.springframework.stereotype.Component;

// import com.project.proo.usreInfo.UserController;

// @Component
// public class ProfileModelAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {

//     @Override
//     public EntityModel<Profile> toModel(Profile profile) {
//         return EntityModel.of(profile,
//                 linkTo(methodOn(profileController.class).getProfile(profile.getId())).withSelfRel(),
//                 linkTo(methodOn(UserController.class).getUser(profile.getUser().getId())).withRel("user"));
//         // Add more links if needed for related resources
//     }
// }
