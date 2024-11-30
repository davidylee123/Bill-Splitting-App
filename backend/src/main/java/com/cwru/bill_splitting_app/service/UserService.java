package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(ObjectId id) {
    return userRepository.findById(id);
  }

  public Optional<User> updateUser(ObjectId id, User userDetails) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setName(userDetails.getName());
      user.setEmail(userDetails.getEmail());
      user.setFriends(userDetails.getFriends());
      return Optional.of(userRepository.save(user));
    }
    return Optional.empty();
  }

  public boolean deleteUser(ObjectId id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      userRepository.delete(userOptional.get());
      return true;
    } else {
      return false;
    }
  }

  public void addFriend(ObjectId userId, ObjectId friendId) {
      User user = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));
      User friend = userRepository.findById(friendId)
              .orElseThrow(() -> new RuntimeException("Friend not found"));

      if (!user.getFriends().contains(friendId)) {
          user.getFriends().add(friendId);
          userRepository.save(user);
      }
  }

  public void removeFriend(ObjectId userId, ObjectId friendId) {
      User user = userRepository.findById(userId)
              .orElseThrow(() -> new RuntimeException("User not found"));

      if (user.getFriends().contains(friendId)) {
          user.getFriends().remove(friendId);
          userRepository.save(user);
      }
  }
}
