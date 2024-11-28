package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Bill;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends MongoRepository<Bill, ObjectId> {

    @Query("{ 'expenses._id': ?0 }")
    Optional<Bill> findByExpensesId(ObjectId expenseId);

    @Query("{ 'users._id': ?0 }")
    Optional<Bill> findByUsersId(ObjectId userId);

    List<Bill> findAllById(ObjectId id);

    List<Bill> findByTitle(String title);
}