package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User("1", "David Lee", "david.lee@example.com");
        User user2 = new User("2", "Jose Kim", "jose.kim@example.com");

        given(userService.getAllUsers()).willReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName").value("David Lee"))
                .andExpect(jsonPath("$[1].userName").value("Jose Kim"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("1", "David Lee", "david.lee@example.com");

        given(userService.createUser(any(User.class))).willReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"1\", \"userName\": \"David Lee\", \"email\": \"david.lee@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("David Lee"));
    }

    @Test
    public void testGetUserById_Found() throws Exception {
        User user = new User("1", "David Lee", "david.lee@example.com");

        given(userService.getUserById("1")).willReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("David Lee"));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        given(userService.getUserById("1")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUser_Found() throws Exception {
        User updatedUser = new User("1", "David Lee Updated", "david.lee@example.com");

        given(userService.updateUser(eq("1"), any(User.class))).willReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"David Lee Updated\", \"email\": \"david.lee@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("David Lee Updated"));
    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        given(userService.updateUser(eq("1"), any(User.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"David Lee Updated\", \"email\": \"david.lee@example.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser_Found() throws Exception {
        given(userService.deleteUser("1")).willReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        given(userService.deleteUser("1")).willReturn(false);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }
}