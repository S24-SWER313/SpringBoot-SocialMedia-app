package com.project.proo;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.proo.profileInfo.Gender;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserController;
import com.project.proo.usreInfo.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class userTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

   

    @InjectMocks
    private UserController userController;

  
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    
  
    @Test
    void testGetAllUsers() throws Exception {

       // String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb2tvbGwiLCJpYXQiOjE3MTE2MzA0MDksImV4cCI6MTcxMTcxNjgwOX0.zAZRUiSC7JJzAMRDXlUzQJOu7SaaMb2znHJaZxHmv1s";
        this.mockMvc.perform(get("/users").header("Authorization", "Bearer " + profileTest.token))
                .andExpect(MockMvcResultMatchers.status().isOk());
       }
     @Test
    void testGetUserById() throws Exception {

       // String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb2tvbGwiLCJpYXQiOjE3MTE2MzA0MDksImV4cCI6MTcxMTcxNjgwOX0.zAZRUiSC7JJzAMRDXlUzQJOu7SaaMb2znHJaZxHmv1s";
        this.mockMvc.perform(get("/users/1").header("Authorization", "Bearer " + profileTest.token))
                .andExpect(MockMvcResultMatchers.status().isOk());
              
    }
   

        @Test
    void testGetUserFriends() throws Exception {
       // String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb2tvbGwiLCJpYXQiOjE3MTE2MzA0MDksImV4cCI6MTcxMTcxNjgwOX0.zAZRUiSC7JJzAMRDXlUzQJOu7SaaMb2znHJaZxHmv1s";
        // User user = new User("newUser", "newuser@example.com", "neSwP!ssw8ord");
        // user.setId(1);
        // user.setFriends(Collections.emptyList());

      //  when(userRepository.findById(1)).thenReturn(Optional.of(user));

        this.mockMvc.perform(get("/users/1/friends").header("Authorization", "Bearer " + profileTest.token))
                .andExpect(MockMvcResultMatchers.status().isOk());
              
    }
    
        @Test
    void testUpdateUser() throws Exception {
        

        User user = new User("newUser", "newuser@example.com", "neSwP!ssw8ord");
        user.setId(1);

        User updatedUser = new User("mk", "updated@test.com", "new!passworK.dd5");
        updatedUser.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1").header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
        @Test
    void testAddDeleteFriendship() throws Exception {
      //  String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb2tvbGwiLCJpYXQiOjE3MTE2MzA0MDksImV4cCI6MTcxMTcxNjgwOX0.zAZRUiSC7JJzAMRDXlUzQJOu7SaaMb2znHJaZxHmv1s";

        User user = new User();
        user.setId(1);


        User friend = new User();
        friend.setId(2);
        
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(friend));

        mockMvc.perform(post("/users/1/friends?friendIds=2").header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        assertNotEquals(user.getFriends().contains(friend),"false");
        assertNotEquals(friend.getFriends().contains(user),"false");
       

        mockMvc.perform(delete("/users/1/friends/2").header("Authorization", "Bearer " + profileTest.token))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertFalse(user.getFriends().contains(friend));
        assertFalse(friend.getFriends().contains(user));
    }





}