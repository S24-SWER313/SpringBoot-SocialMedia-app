package com.project.proo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.proo.postInfo.Post;
import com.project.proo.postInfo.PostModelAssembler;
import com.project.proo.postInfo.PostNotFoundException;
import com.project.proo.postInfo.PostRepository;
import com.project.proo.postInfo.Privacy;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;
import com.project.proo.usreInfo.UserModelAssembler;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
class postTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostModelAssembler postModelAssembler;

    @InjectMocks
    private UserModelAssembler userModelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    
    @Test
    void testGetPosts() throws Exception {
        int userId = 1;
        int postId = 1;
        Post post = new Post();
        post.setId(postId);
        // post.setCaption("Test Caption");
        // post.setDate(LocalDateTime.now());
        // post.setAudiance(Privacy.PRIVATE);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        mockMvc.perform(get("/users/{userId}/posts/{postId}", userId, postId)
                .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isOk());
    
    }

    @Test
    void testAddPost() throws Exception {
        int userId = 1;
        Post post = new Post();
        post.setCaption("Test Caption");
        post.setDate(LocalDateTime.now());
        post.setAudiance(Privacy.PRIVATE);

        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        mockMvc.perform(post("/users/1/posts")
                .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated());
                
    }
          


    @Test
    void testEditPost() throws Exception {
        int postId = 1;
        Post updatedPost = new Post();
        updatedPost.setCaption("Updated Caption");
        updatedPost.setDate(LocalDateTime.now());
        updatedPost.setAudiance(Privacy.PRIVATE);

        when(postRepository.findById(postId)).thenReturn(Optional.of(updatedPost));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);


        mockMvc.perform(put("/users/{userId}/posts/{postId}", 1, postId)
                .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPost)))
                .andExpect(status().isOk());
                
    }

     // @Test////test the methode after adding a post dont test it with all the tests :)
     void testDeletePost() throws Exception {
        int userId = 1;
        int postId = 2;

        mockMvc.perform(delete("/users/{userId}/posts/{postId}", userId, postId)
                .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isNoContent());

                mockMvc.perform(get("/users/{userId}/posts/{postId}", userId, postId)
                .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isNotFound());
    }
    @Test
    void testAddSharePost() throws Exception {
        int userId = 2;
        int postId = 1;
        User user = new User();
        user.setId(userId);
        Post originalPost = new Post();
        originalPost.setId(postId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(originalPost));
        when(postRepository.save(any(Post.class))).thenReturn(originalPost);

        mockMvc.perform(post("/users/{userId}/shared-posts", userId)
                .header("Authorization", "Bearer " + profileTest.token)
                .param("postId", String.valueOf(postId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(originalPost)))
                .andExpect(status().isCreated());
             
    }
    @Test
    void testGetPost() throws Exception {
        int userId = 1;
        int postId = 1;
        User user = new User();
        user.setId(userId);
        Post post = new Post();
        post.setId(postId);
        post.setCaption("Test Caption");
        post.setDate(LocalDateTime.now());
        post.setUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        mockMvc.perform(get("/users/{userId}/posts/{postId}", userId, postId)
        .header("Authorization", "Bearer " + profileTest.token))
        .andExpect(status().isOk());
              
    }
    @Test
    void testGetSharedPosts() throws Exception {
        int userId = 2;
        int postId = 1;
        User user = new User();
        user.setId(userId);
        Post originalPost = new Post();
        originalPost.setId(postId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(originalPost));
        when(postRepository.save(any(Post.class))).thenReturn(originalPost);
      

      // when(user.getPosts()).thenReturn(Collections.singletonList(post));


      mockMvc.perform(post("/users/{userId}/shared-posts", userId)
                .header("Authorization", "Bearer " + profileTest.token)
                .param("postId", String.valueOf(postId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(originalPost)))
                .andExpect(status().isCreated());

     mockMvc.perform(get("/users/{userId}/shared-posts", userId)
     .header("Authorization", "Bearer " + profileTest.token))
     .andExpect(status().isOk());
               
    }

    //@Test // test it after sharing the post dont test it with all the test :)
    void testUnsharePost() throws Exception {
        int userId = 2;
        int sharedPostId=1;
        User user = new User();
        user.setId(userId);
        Post sharedPost = new Post();
        sharedPost.setId(sharedPostId);
        sharedPost.setUser(user);

        when(postRepository.existsById(sharedPostId)).thenReturn(true);
        when(postRepository.findById(sharedPostId)).thenReturn(Optional.of(sharedPost));

     
        mockMvc.perform(delete("/users/{userid}/shared-posts/{sharedPostId}", userId, sharedPostId)
        .header("Authorization", "Bearer " + profileTest.token))
        .andExpect(status().isNoContent());

   
    }
   

    

       
    }





