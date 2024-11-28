package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
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

        user = new User(new ObjectId("644000000000000000000001"), "David Lee", "david@example.com", "hashedPassword", Collections.emptyList());
        friend = new User(new ObjectId("644000000000000000000002"), "Jose Kim", "jose@example.com", "hashedPassword", Collections.emptyList());
    }

    @Test
    void testCreateUser() {
        when(passwordEncoder.encode("hashedPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(user.get_id())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(user.get_id());
        assertTrue(result.isPresent());
        assertEquals(user.getName(), result.get().getName());
        verify(userRepository, times(1)).findById(user.get_id());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(user.get_id())).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(user.get_id());
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(user.get_id());
    }

    @Test
    void testAddFriend_Success() {
        User user = new User(new ObjectId("644000000000000000000001"), "David Lee", "david.lee@example.com", "hashedPassword");
        User friend = new User(new ObjectId("644000000000000000000002"), "Jose Kim", "jose.kim@example.com", "hashedPassword");

        when(userRepository.findById(user.get_id())).thenReturn(Optional.of(user));
        when(userRepository.findById(friend.get_id())).thenReturn(Optional.of(friend));
        when(userRepository.save(user)).thenReturn(user);

        userService.addFriend(user.get_id(), friend.get_id());

        assertTrue(user.getFriends().contains(friend.get_id()));  // Check if the friend was added
        verify(userRepository, times(1)).findById(user.get_id());  // Ensure user repository was called
        verify(userRepository, times(1)).findById(friend.get_id());  // Ensure friend repository was called
        verify(userRepository, times(1)).save(user);  // Ensure user was saved with updated friends list
    }

    @Test
    void testAddFriend_UserNotFound() {
        when(userRepository.findById(user.get_id())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.addFriend(user.get_id(), friend.get_id()));
        verify(userRepository, times(1)).findById(user.get_id());
        verify(userRepository, never()).findById(friend.get_id());
    }

    @Test
    void testAddFriend_FriendNotFound() {
        when(userRepository.findById(user.get_id())).thenReturn(Optional.of(user));
        when(userRepository.findById(friend.get_id())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.addFriend(user.get_id(), friend.get_id()));
        verify(userRepository, times(1)).findById(user.get_id());
        verify(userRepository, times(1)).findById(friend.get_id());
        verify(userRepository, never()).save(any(User.class));
    }
}