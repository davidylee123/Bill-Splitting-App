package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.service.ExpenseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

  @Autowired
  private ExpenseService expenseService;

  @GetMapping
  public ResponseEntity<List<Expense>> getAllExpenses() {
    List<Expense> expenses = expenseService.getAllExpenses();
    if (expenses.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(expenses);
  }

  @PostMapping
  public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
    Expense createdExpense = expenseService.createExpense(expense);
    return ResponseEntity.status(201).body(createdExpense);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Expense> getExpenseById(@PathVariable ObjectId id) {
    try {
      Optional<Expense> expense = expenseService.getExpenseById(id);
      return expense.map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Expense> updateExpense(@PathVariable ObjectId id, @RequestBody Expense expenseDetails) {
    try {
      Optional<Expense> updatedExpense = expenseService.updateExpense(id, expenseDetails);
      return updatedExpense.map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteExpense(@PathVariable("id") ObjectId id) {
    try {
      boolean deleted = expenseService.deleteExpense(id);
      return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
