package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
}
