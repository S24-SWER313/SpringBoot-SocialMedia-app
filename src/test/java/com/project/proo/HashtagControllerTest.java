package com.project.proo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.proo.postInfo.Post;
import com.project.proo.postInfo.PostModelAssembler;
import com.project.proo.postInfo.PostRepository;
import com.project.proo.postInfo.Privacy;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserModelAssembler;
import com.project.proo.usreInfo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HashtagControllerTest {

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
    void testGetPostsByHashtag() throws Exception {
        int userId = 1;
        Post post = new Post();
        post.setId(1);
        post.setCaption("Test Caption #test");
        post.setDate(LocalDateTime.now());
        post.setAudiance(Privacy.PRIVATE);
        String hashtag = "test";
        List<Post> posts = Collections.singletonList(post);
          User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postRepository.findByCaptionContaining(hashtag)).thenReturn(posts);
          


           mockMvc.perform(MockMvcRequestBuilders.post("/users/1/posts")
           .header("Authorization", "Bearer " + profileTest.token)
           .contentType(MediaType.APPLICATION_JSON)
           .content(objectMapper.writeValueAsString(post)))
           .andExpect(status().isCreated());

        mockMvc.perform(get("/hashtags/{hashtag}",hashtag)
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void testGetAllHashtag1() throws Exception {
        mockMvc.perform(get("/hashtags/all")
        .header("Authorization", "Bearer " + profileTest.token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
}

