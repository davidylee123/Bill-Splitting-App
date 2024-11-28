package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

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
        User user1 = new User(new ObjectId("644000000000000000000001"), "David Lee", "david.lee@example.com", "hashedPassword");
        User user2 = new User(new ObjectId("644000000000000000000002"), "Jose Kim", "jose.kim@example.com", "hashedPassword");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("David Lee"))
                .andExpect(jsonPath("$[1].name").value("Jose Kim"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testCreateUser() throws Exception {
        User user = new User(new ObjectId("644000000000000000000001"), "David Lee", "david.lee@example.com", "hashedPassword");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"David Lee\", \"email\": \"david.lee@example.com\", \"password\": \"hashedPassword\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David Lee"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetUserById_Found() throws Exception {
        User user = new User(new ObjectId("644000000000000000000001"), "David Lee", "david.lee@example.com", "hashedPassword");

        when(userService.getUserById(new ObjectId("644000000000000000000001"))).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/644000000000000000000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David Lee"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(new ObjectId("644000000000000000000001"))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/644000000000000000000001"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateUser_Found() throws Exception {
        User updatedUser = new User(new ObjectId("644000000000000000000001"), "David Updated", "updated@example.com", "hashedPassword");

        when(userService.updateUser(eq(new ObjectId("644000000000000000000001")), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/api/users/644000000000000000000001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"David Updated\", \"email\": \"updated@example.com\", \"password\": \"hashedPassword\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David Updated"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateUser_NotFound() throws Exception {
        when(userService.updateUser(eq(new ObjectId("644000000000000000000001")), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/644000000000000000000001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"David Updated\", \"email\": \"updated@example.com\", \"password\": \"hashedPassword\"}")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteUser_Found() throws Exception {
        when(userService.deleteUser(new ObjectId("644000000000000000000001"))).thenReturn(true);

        mockMvc.perform(delete("/api/users/644000000000000000000001")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser(new ObjectId("644000000000000000000001"))).thenReturn(false);

        mockMvc.perform(delete("/api/users/644000000000000000000001")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testAddFriend_Success() throws Exception {
        doNothing().when(userService).addFriend(new ObjectId("644000000000000000000001"), new ObjectId("644000000000000000000002"));

        mockMvc.perform(post("/api/users/644000000000000000000001/friends/644000000000000000000002")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }
}