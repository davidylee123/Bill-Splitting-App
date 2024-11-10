package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {

    @Query("{ 'id': ?0 }")
    Optional<Expense> findByCustomId(String id);

    List<Expense> findByPaidBy(String paidBy);

    List<Expense> findByAmount(double amount);

    List<Expense> findByName(String name);

    @Query("{ 'splitBetween': ?0 }")
    List<Expense> findBySplitBetween(List<String> splitBetween);

    @Query("{ 'splitBetween': { $in: ?0 } }")
    List<Expense> findByAnySplitBetween(List<String> splitBetween);
}