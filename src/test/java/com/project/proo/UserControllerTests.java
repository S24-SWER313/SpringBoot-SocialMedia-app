package com.project.proo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserController;
import com.project.proo.usreInfo.UserModelAssembler;
import com.project.proo.usreInfo.UserRepository;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserModelAssembler userModelAssembler;

    @Test
    @WithMockUser(username = "testuser")
    public void testGetUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        EntityModel<User> entityModel = EntityModel.of(user);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userModelAssembler.toModel(user)).thenReturn(entityModel);

        mockMvc.perform(get("/users/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        List<User> userList = Arrays.asList(user1, user2);
        List<EntityModel<User>> entityModelList = userList.stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(userList);
        when(userModelAssembler.toModel(user1)).thenReturn(EntityModel.of(user1));
        when(userModelAssembler.toModel(user2)).thenReturn(EntityModel.of(user2));

        mockMvc.perform(get("/users"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$._embedded").exists())
               .andExpect(jsonPath("$._embedded.userList").isArray())
               .andExpect(jsonPath("$._embedded.userList.length()").value(2));
    }

    @Test
    public void testGetUserFriends() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        User friend1 = new User();
        friend1.setId(2);
        friend1.setUsername("friend1");

        User friend2 = new User();
        friend2.setId(3);
        friend2.setUsername("friend2");

        user.getFriends().add(friend1);
        user.getFriends().add(friend2);

        List<EntityModel<User>> friendsList = Arrays.asList(EntityModel.of(friend1), EntityModel.of(friend2));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userModelAssembler.toModel(friend1)).thenReturn(EntityModel.of(friend1));
        when(userModelAssembler.toModel(friend2)).thenReturn(EntityModel.of(friend2));

        mockMvc.perform(get("/users/1/friends"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$._embedded").exists())
               .andExpect(jsonPath("$._embedded.friendsList").isArray())
               .andExpect(jsonPath("$._embedded.friendsList.length()").value(2));
    }

    // Additional test methods for other UserController endpoints can be added here
}
