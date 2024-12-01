package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

  @Autowired
  private ExpenseRepository expenseRepository;

  public List<Expense> getAllExpenses() {
    List<Expense> expenses = expenseRepository.findAll();
    return expenses;
  }

  public Expense createExpense(Expense expense) {
    return expenseRepository.save(expense);
  }

  // Find by MongoDB ID (_id)
  public Optional<Expense> getExpenseById(ObjectId id) {
    return expenseRepository.findById(id);
  }

  public List<Expense> findByPaidBy(User paidBy) {
    return expenseRepository.findByPaidBy(paidBy);
  }

  public List<Expense> findByUsers(List<User> users) {
    return expenseRepository.findByUsers(users);
  }

  public List<Expense> findByAmount(double amount) {
    return expenseRepository.findByAmount(amount);
  }

  public List<Expense> findByTitle(String title) {
    return expenseRepository.findByTitle(title);
  }

  public Optional<Expense> updateExpense(ObjectId id, Expense expenseDetails) {
    return expenseRepository.findById(id).map(existingExpense -> {
        existingExpense.setTitle(expenseDetails.getTitle());
        existingExpense.setAmount(expenseDetails.getAmount());
        existingExpense.setPaidBy(expenseDetails.getPaidBy());
        existingExpense.setUsers(expenseDetails.getUsers());
        return expenseRepository.save(existingExpense);
    });
  }

  public boolean deleteExpense(ObjectId id) {
    Optional<Expense> expense = expenseRepository.findById(id);
    if (expense.isPresent()) {
      expenseRepository.delete(expense.get());
      return true;
    } else {
      return false;
    }
  }
}
