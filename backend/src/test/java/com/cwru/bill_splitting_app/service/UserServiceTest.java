package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private ObjectId user1Id;
  private ObjectId user2Id;
  private User user1;
  private User user2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user1Id = new ObjectId();
    user1 = new User();
    user1.set_id(user1Id);
    user1.setName("user1");
    user1.setEmail("user1@example.com");

    user2Id = new ObjectId();
    user2 = new User();
    user2.set_id(user2Id);
    user2.setName("user2");
    user2.setEmail("user2@example.com");
  }

  @Test
  void testCreateUser() {
    when(userRepository.save(any(User.class))).thenReturn(user1);

    User createdUser = userService.createUser(user1);

    assertNotNull(createdUser);
    assertEquals("user1", createdUser.getName());
    verify(userRepository, times(1)).save(user1);
  }

  // Shouldn't be a problem with object_id
  // @Test
  // void testCreateUser_WithExistingId() {
  //   IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
  //       () -> userService.createUser(user1));

  //   assertEquals("User with this ID already exists", exception.getMessage());
  //   verify(userRepository, times(1)).existsById(user1.get_id());
  //   verify(userRepository, times(0)).save(any(User.class));
  // }

  @Test
  void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

    List<User> users = userService.getAllUsers();

    assertEquals(2, users.size());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  void testGetUserById() {
    when(userRepository.findById(user1.get_id())).thenReturn(Optional.of(user1));

    Optional<User> foundUser = userService.getUserById(user1.get_id());

    assertTrue(foundUser.isPresent());
    assertEquals("user1", foundUser.get().getName());
    verify(userRepository, times(1)).findById(user1.get_id());
  }

  @Test
  void testUpdateUser() {
    when(userRepository.findById(user1.get_id())).thenReturn(Optional.of(user1));
    when(userRepository.save(any(User.class))).thenReturn(user1);

    User updatedDetails = new User();
    updatedDetails.set_id(user1Id);
    updatedDetails.setName("Updated User");
    updatedDetails.setEmail("updated@example.com");
    Optional<User> updatedUser = userService.updateUser(user1.get_id(), updatedDetails);

    assertTrue(updatedUser.isPresent());
    assertEquals("Updated User", updatedUser.get().getName());
    assertEquals("updated@example.com", updatedUser.get().getEmail());
    verify(userRepository, times(1)).findById(user1.get_id());
    verify(userRepository, times(1)).save(user1);
  }

  @Test
  void testUpdateUser_NotFound() {
    when(userRepository.findById(user1.get_id())).thenReturn(Optional.empty());

    User updatedDetails = new User();
    updatedDetails.set_id(user1Id);
    updatedDetails.setName("Updated User");
    updatedDetails.setEmail("updated@example.com");
    Optional<User> updatedUser = userService.updateUser(user1.get_id(), updatedDetails);

    assertFalse(updatedUser.isPresent());
    verify(userRepository, times(1)).findById(user1.get_id());
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Test
  void testDeleteUser() {
    when(userRepository.findById(user1.get_id())).thenReturn(Optional.of(user1));
    doNothing().when(userRepository).delete(user1);

    boolean isDeleted = userService.deleteUser(user1.get_id());

    assertTrue(isDeleted);
    verify(userRepository, times(1)).findById(user1.get_id());
    verify(userRepository, times(1)).delete(user1);
  }

  @Test
  void testDeleteUser_NotFound() {
    ObjectId nonexistentId = new ObjectId();
    when(userRepository.findById(nonexistentId)).thenReturn(Optional.empty());

    boolean isDeleted = userService.deleteUser(nonexistentId);

    assertFalse(isDeleted);
    verify(userRepository, times(1)).findById(nonexistentId);
    verify(userRepository, times(0)).delete(any(User.class));
  }
}
