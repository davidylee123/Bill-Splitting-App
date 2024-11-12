package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'id': ?0 }")
    boolean existsByCustomId(String id);

    @Query("{ 'id': ?0 }")
    Optional<User> findByCustomId(String id);

    List<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);
}