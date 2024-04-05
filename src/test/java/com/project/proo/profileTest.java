 package com.project.proo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.proo.profileInfo.Gender;
import com.project.proo.profileInfo.Profile;
import com.project.proo.profileInfo.ProfileModelAssembler;
import com.project.proo.profileInfo.ProfileNotFoundException;
import com.project.proo.profileInfo.ProfileRepository;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserController;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class profileTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileModelAssembler assembler;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private com.project.proo.profileInfo.profileController profileController;
    
    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    public final static String  token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXdVc2VyIiwiaWF0IjoxNzEyMzIzMjYzLCJleHAiOjE3MTI0MDk2NjN9.Gl-odSuNv1E0Xgm1VeobmMKHqPhjQaBB2pZ4il_4rQI";

    @Test
    void testGetAddProfile() throws Exception {
       Profile profile = new Profile("Bio", LocalDate.of(1990, 1, 1), "City", Gender.Male, 30);
        profile.setId(1);

        when(profileRepository.findByUserId(1)).thenReturn(Optional.of(profile));

       // when(profileRepository.save(any(Profile.class))).thenReturn(profile);
       // System.out.println(profile.getBio());

        
    mockMvc.perform(post("/users/1/profiles")
	.header("Authorization", "Bearer " + token)
	.contentType(MediaType.APPLICATION_JSON)
	.content(objectMapper.writeValueAsString(profile)))
	.andExpect(status().isCreated());


        mockMvc.perform(get("/users/1/profiles/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
                assertEquals(profile.getBio(), "Bio");
    }
	@Test
    void testaddFalseProfile() throws Exception {
       Profile profile = new Profile("Bio", LocalDate.of(1990, 1, 1), "City", Gender.Male,6 );
        profile.setId(1);

        when(profileRepository.findByUserId(1)).thenReturn(Optional.of(profile));

       // when(profileRepository.save(any(Profile.class))).thenReturn(profile);
       // System.out.println(profile.getBio());

        
    mockMvc.perform(post("/users/1/profiles")
	.header("Authorization", "Bearer " + token)
	.contentType(MediaType.APPLICATION_JSON)
	.content(objectMapper.writeValueAsString(profile)))
	.andExpect(status().isBadRequest());


    }

    @Test
    void testUpdateProfile() throws Exception {
        int profileId = 1;
        Profile updatedProfile = new Profile();
        updatedProfile.setId(profileId);
        updatedProfile.setBio("Updated Bio");
        updatedProfile.setDob(LocalDate.of(1995, 5, 5));
        updatedProfile.setCity("Updated City");
        updatedProfile.setGender(Gender.Male);
        updatedProfile.setAge(25);

		when(profileRepository.findById(profileId)).thenReturn(Optional.of(new Profile()));
		when(profileRepository.save(any(Profile.class))).thenReturn(updatedProfile);
	
		mockMvc.perform(put("/users/1/profiles/1")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedProfile)))
				.andExpect(status().isOk());
    }

  
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
