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
        Optional<Bill> existingBill = billRepository.findByCustomId(bill.getId());
        if (existingBill.isPresent()) {
            throw new IllegalArgumentException("A bill with this id already exists.");
        }
        return billRepository.save(bill);
    }

    public List<Bill> getBillsByCustomId(String id) {
        return billRepository.findAllById(id);
    }

    public Optional<Bill> getBillByExpenseId(String expenseId) {
        return billRepository.findByExpensesId(expenseId);
    }

    public Optional<Bill> getBillByUserId(String userId) {
        return billRepository.findByUsersId(userId);
    }

    public Optional<Bill> updateBill(String id, Bill billDetails) {
        Optional<Bill> existingBill = billRepository.findByCustomId(id);  // Use custom "id" for updating
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
        Optional<Bill> bill = billRepository.findByCustomId(id);  // Use custom "id" for deletion
        if (bill.isPresent()) {
            billRepository.delete(bill.get());
            return true;
        } else {
            return false;
        }
    }
}