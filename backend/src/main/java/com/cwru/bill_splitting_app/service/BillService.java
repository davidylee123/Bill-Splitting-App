package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Optional<Bill> getBillById(String id) {
        return billRepository.findById(id);
    }

    public Optional<Bill> updateBill(String id, Bill billDetails) {
        Optional<Bill> existingBill = billRepository.findById(id);
        if (existingBill.isPresent()) {
            Bill bill = existingBill.get();
            bill.setTitle(billDetails.getTitle());
            bill.setExpenses(billDetails.getExpenses());
            bill.setUsers(billDetails.getUsers());
            return Optional.of(billRepository.save(bill));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteBill(String id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            billRepository.delete(bill.get());
            return true;
        } else {
            return false;
        }
    }
}