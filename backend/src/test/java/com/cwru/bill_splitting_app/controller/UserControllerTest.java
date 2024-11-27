package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
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
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetAllUsers() throws Exception {
        User user1 = new User("1", "David Lee", "david.lee@example.com");
        User user2 = new User("2", "Jose Kim", "jose.kim@example.com");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userName").value("David Lee"))
                .andExpect(jsonPath("$[1].userName").value("Jose Kim"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateUser() throws Exception {
        User user = new User("1", "David Lee", "david.lee@example.com");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"1\", \"userName\": \"David Lee\", \"email\": \"david.lee@example.com\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("David Lee"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetUserById_Found() throws Exception {
        User user = new User("1", "David Lee", "david.lee@example.com");

        when(userService.getUserById("1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("David Lee"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateUser_Found() throws Exception {
        User updatedUser = new User("1", "David Updated", "updated@example.com");

        when(userService.updateUser(eq("1"), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"David Updated\", \"email\": \"updated@example.com\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("David Updated"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateUser_NotFound() throws Exception {
        when(userService.updateUser(eq("1"), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"David Updated\", \"email\": \"updated@example.com\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteUser_Found() throws Exception {
        when(userService.deleteUser("1")).thenReturn(true);

        mockMvc.perform(delete("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser("1")).thenReturn(false);

        mockMvc.perform(delete("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAddFriend_Success() throws Exception {
        doNothing().when(userService).addFriend("1", "2");

        mockMvc.perform(post("/api/users/1/friends/2")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable String userId, @PathVariable String friendId) {
        try {
            userService.addFriend(userId, friendId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAuthenticateUser_Success() throws Exception {
        User user = new User("1", "David Lee", "david.lee@example.com");

        when(userService.authenticateUser("david.lee@example.com", "plaintextPassword")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/users/authenticate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "david.lee@example.com")
                        .param("password", "plaintextPassword")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("David Lee"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAuthenticateUser_InvalidCredentials() throws Exception {
        when(userService.authenticateUser("david.lee@example.com", "wrongPassword")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/authenticate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "david.lee@example.com")
                        .param("password", "wrongPassword")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
    }
}