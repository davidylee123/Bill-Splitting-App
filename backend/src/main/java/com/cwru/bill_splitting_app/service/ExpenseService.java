package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Optional<Expense> getExpenseById(String id) {
        return expenseRepository.findById(id);
    }

    public Optional<Expense> updateExpense(String id, Expense expenseDetails) {
        Optional<Expense> existingExpense = expenseRepository.findById(id);
        if (existingExpense.isPresent()) {
            Expense expense = existingExpense.get();
            expense.setName(expenseDetails.getName());
            expense.setAmount(expenseDetails.getAmount());
            expense.setPaidBy(expenseDetails.getPaidBy());
            expense.setSplitBetween(expenseDetails.getSplitBetween());
            return Optional.of(expenseRepository.save(expense));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteExpense(String id) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isPresent()) {
            expenseRepository.delete(expense.get());
            return true;
        } else {
            return false;
        }
    }
}