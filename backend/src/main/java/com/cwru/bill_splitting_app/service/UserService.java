package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (user.getPasswordHash() != null) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findByCustomId(id);
    }

    public Optional<User> updateUser(String id, User userDetails) {
        Optional<User> userOptional = userRepository.findByCustomId(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserName(userDetails.getUserName());
            user.setEmail(userDetails.getEmail());
            if (userDetails.getPasswordHash() != null) {
                user.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
            }
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    public boolean deleteUser(String id) {
        Optional<User> userOptional = userRepository.findByCustomId(id);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        }
        return false;
    }

    public void addFriend(String userId, String friendId) {
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("A user cannot add themselves as a friend.");
        }

        User user = userRepository.findByCustomId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findByCustomId(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        if (!user.getFriends().contains(friendId)) {
            user.getFriends().add(friendId);
            userRepository.save(user);
        }

        if (!friend.getFriends().contains(userId)) {
            friend.getFriends().add(userId);
            userRepository.save(friend);
        }
    }

    public void removeFriend(String userId, String friendId) {
        User user = userRepository.findByCustomId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findByCustomId(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        if (user.getFriends().contains(friendId)) {
            user.getFriends().remove(friendId);
            userRepository.save(user);
        }

        if (friend.getFriends().contains(userId)) {
            friend.getFriends().remove(userId);
            userRepository.save(friend);
        }
    }

    public Optional<User> authenticateUser(String email, String rawPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}