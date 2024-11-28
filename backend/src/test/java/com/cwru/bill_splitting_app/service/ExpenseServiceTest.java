package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.repository.ExpenseRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        expense1 = new Expense(new ObjectId("644000000000000000000001"), "Groceries", 50.0, "David Lee",
                Arrays.asList("David Lee", "Jose Kim"));

        expense2 = new Expense(new ObjectId("644000000000000000000002"), "Electricity", 75.5, "Jose Kim",
                Arrays.asList("David Lee", "Jose Kim"));
    }

    @Test
    void testGetAllExpenses() {
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense1, expense2));

        List<Expense> expenses = expenseService.getAllExpenses();

        assertEquals(2, expenses.size());
        verify(expenseRepository, times(1)).findAll();
    }

    @Test
    void testCreateExpense() {
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense1);

        Expense savedExpense = expenseService.createExpense(expense1);

        assertNotNull(savedExpense);
        assertEquals("Groceries", savedExpense.getName());
        verify(expenseRepository, times(1)).save(expense1);
    }

    @Test
    void testGetExpenseById() {
        when(expenseRepository.findById(new ObjectId("644000000000000000000001"))).thenReturn(Optional.of(expense1));

        Optional<Expense> foundExpense = expenseService.getExpenseById(new ObjectId("644000000000000000000001"));

        assertTrue(foundExpense.isPresent());
        assertEquals("Groceries", foundExpense.get().getName());
        verify(expenseRepository, times(1)).findById(new ObjectId("644000000000000000000001"));
    }

    @Test
    void testFindByPaidBy() {
        when(expenseRepository.findByPaidBy("David Lee")).thenReturn(Arrays.asList(expense1));

        List<Expense> expenses = expenseService.findByPaidBy("David Lee");

        assertEquals(1, expenses.size());
        assertEquals("Groceries", expenses.get(0).getName());
        verify(expenseRepository, times(1)).findByPaidBy("David Lee");
    }

    @Test
    void testFindBySplitBetween() {
        when(expenseRepository.findBySplitBetween(Arrays.asList("David Lee", "Jose Kim")))
                .thenReturn(Arrays.asList(expense1, expense2));

        List<Expense> expenses = expenseService.findBySplitBetween(Arrays.asList("David Lee", "Jose Kim"));

        assertEquals(2, expenses.size());
        verify(expenseRepository, times(1)).findBySplitBetween(Arrays.asList("David Lee", "Jose Kim"));
    }

    @Test
    void testUpdateExpense() {
        when(expenseRepository.findById(new ObjectId("644000000000000000000001"))).thenReturn(Optional.of(expense1));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense1);

        Expense updatedExpenseDetails = new Expense(new ObjectId("644000000000000000000001"), "Updated Groceries", 60.0,
                "David Lee", Arrays.asList("David Lee", "Jose Kim"));

        Optional<Expense> updatedExpense = expenseService.updateExpense(new ObjectId("644000000000000000000001"), updatedExpenseDetails);

        assertTrue(updatedExpense.isPresent());
        assertEquals("Updated Groceries", updatedExpense.get().getName());
        assertEquals(60.0, updatedExpense.get().getAmount());
        verify(expenseRepository, times(1)).findById(new ObjectId("644000000000000000000001"));
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testDeleteExpense() {
        when(expenseRepository.findById(new ObjectId("644000000000000000000001"))).thenReturn(Optional.of(expense1));
        doNothing().when(expenseRepository).delete(expense1);

        boolean isDeleted = expenseService.deleteExpense(new ObjectId("644000000000000000000001"));

        assertTrue(isDeleted);
        verify(expenseRepository, times(1)).findById(new ObjectId("644000000000000000000001"));
        verify(expenseRepository, times(1)).delete(expense1);
    }

    @Test
    void testDeleteExpense_NotFound() {
        when(expenseRepository.findById(new ObjectId("644000000000000000000003"))).thenReturn(Optional.empty());

        boolean isDeleted = expenseService.deleteExpense(new ObjectId("644000000000000000000003"));

        assertFalse(isDeleted);
        verify(expenseRepository, times(1)).findById(new ObjectId("644000000000000000000003"));
        verify(expenseRepository, times(0)).delete(any(Expense.class));
    }
}