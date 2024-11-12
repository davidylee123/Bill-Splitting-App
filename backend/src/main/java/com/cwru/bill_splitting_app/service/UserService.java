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
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with this ID already exists");
        }
        if (user.get_id() == null) {
            user.set_id(new ObjectId().toString());
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(String id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserName(userDetails.getUserName());
            user.setEmail(userDetails.getEmail());
            return Optional.of(userRepository.save(user));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        } else {
            return false;
        }
    }
}
