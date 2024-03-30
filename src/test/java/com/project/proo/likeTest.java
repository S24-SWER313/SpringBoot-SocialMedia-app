package com.project.proo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.proo.postInfo.*;
import com.project.proo.usreInfo.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class likeTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentLikeAssembler commentLikeAssembler;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostLikeAssembler postLikeAssembler;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllLikesForComment() throws Exception {
        int postId = 1;
        int commentId = 1;

        mockMvc.perform(get("/posts/{postId}/comments/{commentId}/likes", postId, commentId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllLikesForPost() throws Exception {
        int postId = 1;

        mockMvc.perform(get("/posts/{postId}/likes", postId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isOk());
    }

    @Test
    void testAddLikeForComment() throws Exception {
        int userId = 1;
        int commentId = 1;

        CommentLike newCommentLike = new CommentLike();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(commentLikeRepository.save(any(CommentLike.class))).thenReturn(newCommentLike);

        mockMvc.perform(post("/posts/17/comments/{commentId}/users/{userId}/likes", commentId, userId)
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCommentLike)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddLikeForPosts() throws Exception {
        int userId = 1;
        int postId = 1;

        PostLike newPostLike = new PostLike();

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(postLikeRepository.save(any(PostLike.class))).thenReturn(newPostLike);

        mockMvc.perform(post("/posts/{postId}/users/{userId}/likes", postId, userId)
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPostLike)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetCommentLike() throws Exception {
        int postId = 1;
        int commentId = 1;
        int likeId = 2;
        int userId = 1;
   

        CommentLike newCommentLike = new CommentLike();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(commentLikeRepository.save(any(CommentLike.class))).thenReturn(newCommentLike);

        mockMvc.perform(post("/posts/17/comments/{commentId}/users/{userId}/likes", commentId, userId)
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCommentLike)))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/posts/{postId}/comments/{commentId}/likes/{likeId}", postId, commentId, likeId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPostLike() throws Exception {
        int postId = 1;
        int likeId = 1;
        int userId = 1;
      

        PostLike newPostLike = new PostLike();

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(postLikeRepository.save(any(PostLike.class))).thenReturn(newPostLike);

        mockMvc.perform(post("/posts/{postId}/users/{userId}/likes", postId, userId)
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPostLike)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/posts/{postId}/likes/{likeId}", postId, likeId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isOk());
    }

   // @Test //test this after adding the comment dont test it with all of the tests :)
    void testDeleteCommentLike() throws Exception {
        int postId = 1;
        int commentId = 1;
        int likeId = 2;

        mockMvc.perform(delete("/posts/{postId}/comments/{commentId}/likes/{likeId}", postId, commentId, likeId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isNoContent());
    }

   // @Test //test this after adding the comment dont test it with all of the tests :)
    void testDeletePostLike() throws Exception {
        int postId = 1;
        int likeId = 1;

        mockMvc.perform(delete("/posts/{postId}/likes/{likeId}", postId, likeId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isNoContent());
    }
}

