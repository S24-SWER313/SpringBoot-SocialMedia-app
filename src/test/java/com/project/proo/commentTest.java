package com.project.proo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.proo.postInfo.Comment;
import com.project.proo.postInfo.CommentRepository;
import com.project.proo.postInfo.CommentModelAssembler;
import com.project.proo.postInfo.Post;
import com.project.proo.postInfo.PostRepository;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserNotFoundException;
import com.project.proo.usreInfo.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class commentTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentModelAssembler commentModelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetComment() throws Exception {
        int postId = 17;
        int commentId = 1;
        Comment comment = new Comment();
        comment.setId(commentId);
        // comment.setCommentContent("Test Comment");
        // comment.setDate(LocalDateTime.now());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/posts/{postId}/comments/{commentId}", postId, commentId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCommentsByPost() throws Exception {
        int postId = 17;

        mockMvc.perform(get("/posts/{postId}/comments", postId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isOk());
    }

    @Test
    void testAddComment() throws Exception {
        int postId = 17;
        int userId = 1;
        Comment newComment = new Comment();
        newComment.setCommentContent("New Comment");
        newComment.setDate(LocalDateTime.now());
        newComment.setPost(new Post());
        newComment.setUser(new User());

        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);

        mockMvc.perform(post("/posts/{postId}/comments/users/{commentUserId}", postId, userId)
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isCreated());
    }

    @Test
    void testEditComment() throws Exception {
        int commentId = 1;
        Comment updatedComment = new Comment();
        updatedComment.setCommentContent("Updated Comment");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(new Comment()));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        mockMvc.perform(put("/posts/17/comments/{commentId}", commentId)
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedComment)))
                .andExpect(status().isOk());
    }

   // @Test //test this after adding the comment dont test it with all of the tests :)
    void testDeleteComment() throws Exception {
        int commentId = 2;

        mockMvc.perform(delete("/posts/1/comments/{commentId}", commentId)
        .header("Authorization", "Bearer " + profileTest.token))
                .andExpect(status().isNoContent());
    }
}

