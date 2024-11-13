package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.repository.ExpenseRepository;
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
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        expense1 = new Expense("exp1", "Groceries", 50.0, "David Lee", Arrays.asList("David Lee", "Jose Kim"));
        expense2 = new Expense("exp2", "Electricity", 75.5, "Jose Kim", Arrays.asList("David Lee", "Jose Kim"));
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
        when(expenseRepository.findById("exp1")).thenReturn(Optional.of(expense1));

        Optional<Expense> foundExpense = expenseService.getExpenseById("exp1");

        assertTrue(foundExpense.isPresent());
        assertEquals("Groceries", foundExpense.get().getName());
        verify(expenseRepository, times(1)).findById("exp1");
    }

    @Test
    void testGetExpenseByCustomId() {
        when(expenseRepository.findByCustomId("exp1")).thenReturn(Optional.of(expense1));

        Optional<Expense> foundExpense = expenseService.getExpenseByCustomId("exp1");

        assertTrue(foundExpense.isPresent());
        assertEquals("Groceries", foundExpense.get().getName());
        verify(expenseRepository, times(1)).findByCustomId("exp1");
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
    void testFindByAmount() {
        when(expenseRepository.findByAmount(50.0)).thenReturn(Arrays.asList(expense1));

        List<Expense> expenses = expenseService.findByAmount(50.0);

        assertEquals(1, expenses.size());
        assertEquals("Groceries", expenses.get(0).getName());
        verify(expenseRepository, times(1)).findByAmount(50.0);
    }

    @Test
    void testFindByName() {
        when(expenseRepository.findByName("Groceries")).thenReturn(Arrays.asList(expense1));

        List<Expense> expenses = expenseService.findByName("Groceries");

        assertEquals(1, expenses.size());
        assertEquals("Groceries", expenses.get(0).getName());
        verify(expenseRepository, times(1)).findByName("Groceries");
    }

    @Test
    void testUpdateExpense() {
        when(expenseRepository.findById("exp1")).thenReturn(Optional.of(expense1));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense1);

        Expense updatedExpenseDetails = new Expense("exp1", "Updated Groceries", 60.0, "David Lee", Arrays.asList("David Lee", "Jose Kim"));
        Optional<Expense> updatedExpense = expenseService.updateExpense("exp1", updatedExpenseDetails);

        assertTrue(updatedExpense.isPresent());
        assertEquals("Updated Groceries", updatedExpense.get().getName());
        assertEquals(60.0, updatedExpense.get().getAmount());
        verify(expenseRepository, times(1)).findById("exp1");
        verify(expenseRepository, times(1)).save(expense1);
    }

    @Test
    void testDeleteExpense() {
        when(expenseRepository.findById("exp1")).thenReturn(Optional.of(expense1));
        doNothing().when(expenseRepository).delete(expense1);

        boolean isDeleted = expenseService.deleteExpense("exp1");

        assertTrue(isDeleted);
        verify(expenseRepository, times(1)).findById("exp1");
        verify(expenseRepository, times(1)).delete(expense1);
    }

    @Test
    void testDeleteExpense_NotFound() {
        when(expenseRepository.findById("exp3")).thenReturn(Optional.empty());

        boolean isDeleted = expenseService.deleteExpense("exp3");

        assertFalse(isDeleted);
        verify(expenseRepository, times(1)).findById("exp3");
        verify(expenseRepository, times(0)).delete(any(Expense.class));
    }
}