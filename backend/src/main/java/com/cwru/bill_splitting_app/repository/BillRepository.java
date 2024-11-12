package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends MongoRepository<Bill, String> {

    @Query("{ 'id': ?0 }")
    Optional<Bill> findByCustomId(String id);

    @Query("{ 'expenses.id': ?0 }")
    Optional<Bill> findByExpensesId(String expenseId);

    Optional<Bill> findByUsersId(String userId);

    List<Bill> findAllById(String id);
}