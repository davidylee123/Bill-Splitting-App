package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private User friend;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("user1", "David Lee", "david@example.com", "hashedPassword", null);
        friend = new User("user2", "Jose Kim", "jose@example.com", "hashedPassword", null);
    }

    @Test
    void testCreateUser_Success() {
        when(passwordEncoder.encode("plaintextPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(new User("user1", "David Lee", "david@example.com", "plaintextPassword", null));

        assertNotNull(createdUser);
        assertEquals("hashedPassword", createdUser.getPasswordHash());
        verify(passwordEncoder, times(1)).encode("plaintextPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById("user1");

        assertTrue(result.isPresent());
        assertEquals("David Lee", result.get().getUserName());
        verify(userRepository, times(1)).findByCustomId("user1");
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById("user1");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByCustomId("user1");
    }

    @Test
    void testUpdateUser_Success() {
        User updatedDetails = new User("user1", "David Updated", "updated@example.com", null, null);
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedDetails);

        Optional<User> result = userService.updateUser("user1", updatedDetails);

        assertTrue(result.isPresent());
        assertEquals("David Updated", result.get().getUserName());
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.empty());

        Optional<User> result = userService.updateUser("user1", new User("user1", "Updated", "updated@example.com", null, null));

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        boolean result = userService.deleteUser("user1");

        assertTrue(result);
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.empty());

        boolean result = userService.deleteUser("user1");

        assertFalse(result);
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testAddFriend_Success() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.of(user));
        when(userRepository.findByCustomId("user2")).thenReturn(Optional.of(friend));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addFriend("user1", "user2");

        assertTrue(user.getFriends().contains("user2"));
        assertTrue(friend.getFriends().contains("user1"));
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, times(1)).findByCustomId("user2");
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void testAddFriend_UserNotFound() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.addFriend("user1", "user2"));
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, never()).findByCustomId("user2");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRemoveFriend_Success() {
        user.getFriends().add("user2");
        friend.getFriends().add("user1");

        when(userRepository.findByCustomId("user1")).thenReturn(Optional.of(user));
        when(userRepository.findByCustomId("user2")).thenReturn(Optional.of(friend));

        userService.removeFriend("user1", "user2");

        assertFalse(user.getFriends().contains("user2"));
        assertFalse(friend.getFriends().contains("user1"));
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, times(1)).findByCustomId("user2");
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    void testRemoveFriend_UserNotFound() {
        when(userRepository.findByCustomId("user1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.removeFriend("user1", "user2"));
        verify(userRepository, times(1)).findByCustomId("user1");
        verify(userRepository, never()).findByCustomId("user2");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_Success() {
        when(userRepository.findByEmail("david@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plaintextPassword", "hashedPassword")).thenReturn(true);

        Optional<User> result = userService.authenticateUser("david@example.com", "plaintextPassword");

        assertTrue(result.isPresent());
        assertEquals("David Lee", result.get().getUserName());
        verify(userRepository, times(1)).findByEmail("david@example.com");
        verify(passwordEncoder, times(1)).matches("plaintextPassword", "hashedPassword");
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.authenticateUser("unknown@example.com", "plaintextPassword");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("unknown@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        when(userRepository.findByEmail("david@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        Optional<User> result = userService.authenticateUser("david@example.com", "wrongPassword");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("david@example.com");
        verify(passwordEncoder, times(1)).matches("wrongPassword", "hashedPassword");
    }
}