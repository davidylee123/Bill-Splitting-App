package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.repository.BillRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

  @Autowired
  private BillRepository billRepository;

  public List<Bill> getAllBills() {
    List<Bill> bills = billRepository.findAll();

    for (Bill bill : bills) {
      if (bill.getExpenses() != null) {
        for (Expense expense : bill.getExpenses()) {
          if (expense == null) {
            throw new IllegalArgumentException("Invalid expense detected: " + expense);
          }
        }
      }
    }

    return bills;
  }

  public Bill createBill(Bill bill) {
    Optional<Bill> existingBill = billRepository.findById(bill.get_id());
    if (existingBill.isPresent()) {
      throw new IllegalArgumentException("A bill with this id already exists.");
    }
    return billRepository.save(bill);
  }

  public Optional<Bill> getBillByExpenseId(ObjectId expenseId) {
    Optional<Bill> bill = billRepository.findByExpensesId(expenseId);
    return bill;
  }

  public Optional<Bill> getBillByUserId(ObjectId userId) {
    Optional<Bill> bill = billRepository.findByUsersId(userId);
    return bill;
  }

  public Optional<Bill> getBillById(ObjectId id) {
    return billRepository.findById(id);
  }

  public Optional<Bill> updateBill(ObjectId id, Bill billDetails) {
    Optional<Bill> existingBill = billRepository.findById(id); // Use custom "id" for updating
    if (existingBill.isPresent()) {
      Bill bill = existingBill.get();
      bill.setTitle(billDetails.getTitle());
      bill.setExpenses(billDetails.getExpenses());
      bill.setUsers(billDetails.getUsers());
      return Optional.of(billRepository.save(bill));
    }
    return Optional.empty();
  }

  public boolean deleteBill(ObjectId id) {
    Optional<Bill> bill = billRepository.findById(id); // Use custom "id" for deletion
    if (bill.isPresent()) {
      billRepository.delete(bill.get());
      return true;
    }
    return false;
  }
}
