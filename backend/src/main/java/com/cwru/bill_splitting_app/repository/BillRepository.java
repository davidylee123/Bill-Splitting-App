package com.cwru.bill_splitting_app.repository;

import com.cwru.bill_splitting_app.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BillRepository extends MongoRepository<Bill, String> {
    List<Bill> findByUsersUserId(String userId);  
}
