package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.createUser(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable ObjectId id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable ObjectId id, @RequestBody User userDetails) {
    Optional<User> updatedUser = userService.updateUser(id, userDetails);
    return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable ObjectId id) {
    if (userService.deleteUser(id)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("/{userId}/friends/{friendId}")
  public ResponseEntity<Void> addFriend(@PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
    try {
      userService.addFriend(userId, friendId);
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{userId}/friends/{friendId}")
  public ResponseEntity<Void> removeFriend(@PathVariable ObjectId userId, @PathVariable ObjectId friendId) {
    try {
      userService.removeFriend(userId, friendId);
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
