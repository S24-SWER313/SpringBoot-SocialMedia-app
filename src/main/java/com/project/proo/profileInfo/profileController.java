package com.project.proo.profileInfo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

// Other imports...

@RestController
public class profileController {

    private final ProfileRepository profileRepository;
    private final ProfileModelAssembler assembler;

    public profileController(ProfileRepository profileRepository, ProfileModelAssembler assembler) {
        this.profileRepository = profileRepository;
        this.assembler = assembler;
    }

    @PutMapping("/users/{userId}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable Integer userId, @RequestBody Profile updatedProfile) {
        // Retrieve the user's profile by user ID
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        // Update profile information
        profile.setBio(updatedProfile.getBio());
        profile.setDob(updatedProfile.getDob());
        profile.setCity(updatedProfile.getCity());
        profile.setGender(updatedProfile.getGender());
        // Update other fields as needed...

        // Save the updated profile
        Profile savedProfile = profileRepository.save(profile);

        // Convert the saved profile to an EntityModel
        EntityModel<Profile> entityModel = assembler.toModel(savedProfile);

        // Return response with the updated profile
        return ResponseEntity.ok(entityModel);
    }
}
