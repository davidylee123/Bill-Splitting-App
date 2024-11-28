package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Expense;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, ObjectId> {

    List<Expense> findByPaidBy(String paidBy);

    @Query("{ 'splitBetween' : { $in : ?0 } }")
    List<Expense> findBySplitBetween(List<String> splitBetween);

    List<Expense> findByAmount(double amount);

    List<Expense> findByName(String name);
}