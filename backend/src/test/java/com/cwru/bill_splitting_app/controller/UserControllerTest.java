package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

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

  @Autowired
  private ObjectMapper objectMapper;

  private User user1;
  private User user2;
  private ObjectId user1Id;
  private ObjectId user2Id;

  @BeforeEach
  public void setup() {
    user1Id = new ObjectId();
    user1 = new User();
    user1.set_id(user1Id);
    user1.setName("David Lee");
    user1.setEmail("david.lee@example.com");

    user2Id = new ObjectId();
    user2 = new User();
    user2.set_id(user2Id);
    user2.setName("Jose Kim");
    user2.setEmail("jose.kim@example.com");

    user1.setFriends(Arrays.asList(user1Id));
    user2.setFriends(Arrays.asList(user2Id));
  }

  @Test
  public void testGetAllUsers() throws Exception {
    given(userService.getAllUsers()).willReturn(Arrays.asList(user1, user2));

    mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("David Lee"))
            .andExpect(jsonPath("$[1].name").value("Jose Kim"));
  }

  @Test
  public void testCreateUser() throws Exception {
    given(userService.createUser(any(User.class))).willReturn(user1);

    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \"David Lee\", \"email\": \"david.lee@example.com\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("David Lee"));
  }

  @Test
  public void testGetUserById_Found() throws Exception {
    given(userService.getUserById(user1Id)).willReturn(Optional.of(user1));

    mockMvc.perform(get("/api/users/" + user1Id.toHexString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("David Lee"));
  }

  @Test
  public void testGetUserById_NotFound() throws Exception {
    ObjectId non_existing_user_id = new ObjectId();
    given(userService.getUserById(non_existing_user_id)).willReturn(Optional.empty());

    mockMvc.perform(get("/api/users/" + user1Id.toHexString()))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateUser_Found() throws Exception {
    User updatedUser = new User();
    updatedUser.set_id(user1Id);
    updatedUser.setName("David Lee Updated");
    updatedUser.setEmail("david.lee@example.com");
    updatedUser.setFriends(Collections.singletonList(new ObjectId("64c87da267e2a12b3c5d67e9")));

    given(userService.updateUser(eq(user1Id), any(User.class))).willReturn(Optional.of(updatedUser));

    mockMvc.perform(put("/api/users/{id}", user1Id.toHexString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("David Lee Updated"))
            .andExpect(jsonPath("$.email").value("david.lee@example.com"))
            .andExpect(jsonPath("$.friends[0]").value("64c87da267e2a12b3c5d67e9"));
  }

  @Test
  public void testUpdateUser_NotFound() throws Exception {
    ObjectId non_existing_user_id = new ObjectId();
    given(userService.updateUser(eq(user1Id), any(User.class))).willReturn(Optional.empty());

    mockMvc.perform(put("/api/users/" + non_existing_user_id.toHexString())
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"name\": \"David Lee Updated\", \"email\": \"david.lee@example.com\"}"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteUser_Found() throws Exception {
    given(userService.deleteUser(user1Id)).willReturn(true);

    mockMvc.perform(delete("/api/users/" + user1Id.toHexString()))
        .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteUser_NotFound() throws Exception {
    ObjectId non_existing_user_id = new ObjectId();
    given(userService.deleteUser(non_existing_user_id)).willReturn(false);

    mockMvc.perform(delete("/api/users/" + non_existing_user_id.toHexString()))
        .andExpect(status().isNotFound());
  }
}
