package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.repository.ExpenseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<Expense> getExpenseById(ObjectId id) {
        return expenseRepository.findById(id);
    }

    public List<Expense> findByPaidBy(String paidBy) {
        return expenseRepository.findByPaidBy(paidBy);
    }

    public List<Expense> findBySplitBetween(List<String> splitBetweenUsers) {
        return expenseRepository.findBySplitBetween(splitBetweenUsers);
    }

    public List<Expense> findByAmount(double amount) {
        return expenseRepository.findByAmount(amount);
    }

    public List<Expense> findByName(String name) {
        return expenseRepository.findByName(name);
    }

    public Optional<Expense> updateExpense(ObjectId id, Expense expenseDetails) {
        return expenseRepository.findById(id).map(existingExpense -> {
            existingExpense.setName(expenseDetails.getName());
            existingExpense.setAmount(expenseDetails.getAmount());
            existingExpense.setPaidBy(expenseDetails.getPaidBy());
            existingExpense.setSplitBetween(expenseDetails.getSplitBetween());
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