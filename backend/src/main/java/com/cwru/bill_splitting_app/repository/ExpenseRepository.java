package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, ObjectId> {

    List<Expense> findByPaidBy(User paidBy);

    List<Expense> findByAmount(double amount);

    List<Expense> findByTitle(String title);

    @Query("{ 'users': ?0 }")
    List<Expense> findByUsers(List<User> users);
}
