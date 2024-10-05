package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
