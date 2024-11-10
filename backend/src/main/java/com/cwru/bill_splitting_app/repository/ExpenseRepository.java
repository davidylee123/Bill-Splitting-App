package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {

    // Find expense by custom ID
    @Query("{ 'id': ?0 }")
    Optional<Expense> findByCustomId(String id);

    // Find expenses by the paidBy field
    List<Expense> findByPaidBy(String paidBy);

    // Find expenses by amount
    List<Expense> findByAmount(double amount);

    // Find expenses by name
    List<Expense> findByName(String name);

    // Find expenses where the splitBetween field exactly matches the given list
    @Query("{ 'splitBetween': ?0 }")
    List<Expense> findBySplitBetween(List<String> splitBetween);

    // Find expenses where at least one entry in splitBetween matches any item in the given list
    @Query("{ 'splitBetween': { $in: ?0 } }")
    List<Expense> findByAnySplitBetween(List<String> splitBetween);
}