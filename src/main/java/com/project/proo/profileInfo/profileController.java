package com.project.proo.profileInfo;

import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserController;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

// Other imports...

@RestController
public class profileController {

    private final ProfileRepository profileRepository;
    private final ProfileModelAssembler assembler;
    private final UserRepository userRepository;

    public profileController(ProfileRepository profileRepository, ProfileModelAssembler assembler, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.assembler = assembler;
        this.userRepository=userRepository;
    }

      @GetMapping("/users/{userId}/profiles")
      public ResponseEntity<?> getProfile(@PathVariable Integer userId) {
        // Retrieve the user's profile by user ID
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        EntityModel<Profile> entityModel = assembler.toModel(profile);

        return ResponseEntity.ok(entityModel);
    }


    @PutMapping("/users/{userId}/profiles")
    public ResponseEntity<?> updateProfile(@PathVariable Integer userId, @RequestBody Profile updatedProfile) {
       
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        profile.setBio(updatedProfile.getBio());
        profile.setDob(updatedProfile.getDob());
        profile.setCity(updatedProfile.getCity());
        profile.setGender(updatedProfile.getGender());

        Profile savedProfile = profileRepository.save(profile);

        EntityModel<Profile> entityModel = assembler.toModel(savedProfile);

        return ResponseEntity.ok(entityModel);
    }
    
    @PostMapping("/users/{userId}/profiles")
    public ResponseEntity<?> addProfile(@PathVariable Integer userId, @RequestBody Profile newProfile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    
        newProfile.setUser(user);
    
        Profile savedProfile = profileRepository.save(newProfile);
        EntityModel<Profile> entityModel = assembler.toModel(savedProfile);
    
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    
}
