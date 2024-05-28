package com.project.proo.profileInfo;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.proo.postInfo.Post;
import com.project.proo.postInfo.PostController;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@RestController
@CrossOrigin
@RequestMapping("/users/{userId}")
public class profileController {

    private final ProfileRepository profileRepository;
    private final ProfileModelAssembler assembler;
    private final UserRepository userRepository;

    public profileController(ProfileRepository profileRepository, ProfileModelAssembler assembler, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.assembler = assembler;
        this.userRepository = userRepository;
    }

    @GetMapping("/profiles/{profileId}")
    public ResponseEntity<?> getProfile(@PathVariable Integer profileId, @PathVariable Integer userId) {
        Profile profile = profileRepository.findByUserId(userId)
        .orElseThrow(() -> new ProfileNotFoundException(userId));

        EntityModel<Profile> entityModel = assembler.toModel(profile);

        return ResponseEntity.ok(entityModel);
    }
    

    @PutMapping("/profiles/{profileId}")
    public ResponseEntity<?> updateProfile(@PathVariable Integer profileId, @Valid @RequestBody Profile updatedProfile) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));

        profile.setBio(updatedProfile.getBio());
        profile.setDob(updatedProfile.getDob());
        profile.setCity(updatedProfile.getCity());
        profile.setGender(updatedProfile.getGender());
        profile.setAge(updatedProfile.getAge());

        Profile savedProfile = profileRepository.save(profile);

        EntityModel<Profile> entityModel = assembler.toModel(savedProfile);

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("/profiles")
    public ResponseEntity<?> addProfile(@PathVariable Integer userId, @Valid @RequestBody Profile newProfile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        newProfile.setUser(user);

        Profile savedProfile = profileRepository.save(newProfile);
        EntityModel<Profile> entityModel = assembler.toModel(savedProfile);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/profiles")
    public CollectionModel<EntityModel<Profile>> all() {
        List<EntityModel<Profile>> profiles = profileRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(profiles, linkTo(methodOn(profileController.class).all()).withSelfRel());
    }
}