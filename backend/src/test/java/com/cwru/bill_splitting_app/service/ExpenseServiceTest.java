package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.bson.types.ObjectId;

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

  private ObjectId expense1Id;
  private ObjectId expense2Id;
  private Expense expense1;
  private Expense expense2;

  private ObjectId user1Id;
  private ObjectId user2Id;
  private User user1;
  private User user2;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    user1Id = new ObjectId();
    user1 = new User();
    user1.set_id(user1Id);
    user1.setName("David Lee");
    user1.setEmail("david.lee@example.com");

    user2Id = new ObjectId();
    user2 = new User();
    user2.set_id(user2Id);
    user2.setName("David Lee");
    user2.setEmail("jose.kim@example.com");

    expense1 = new Expense();
    expense1Id = new ObjectId();
    expense1.set_id(expense1Id);
    expense1.setTitle("Groceries");
    expense1.setAmount(50.0);
    expense1.setPaidBy(user1);
    expense1.setUsers(Arrays.asList(user1, user2));

    expense2 = new Expense();
    expense2Id = new ObjectId();
    expense2.set_id(expense2Id);
    expense2.setTitle("Electricity");
    expense2.setAmount(75.5);
    expense2.setPaidBy(user2);
    expense2.setUsers(Arrays.asList(user1, user2));

    user1.setFriends(Arrays.asList(user2Id));
    user2.setFriends(Arrays.asList(user1Id));
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
    assertEquals("Groceries", savedExpense.getTitle());
    verify(expenseRepository, times(1)).save(expense1);
  }

  @Test
  void testGetExpenseById() {
    when(expenseRepository.findById(expense1Id)).thenReturn(Optional.of(expense1));

    Optional<Expense> foundExpense = expenseService.getExpenseById(expense1Id);

    assertTrue(foundExpense.isPresent());
    assertEquals("Groceries", foundExpense.get().getTitle());
    verify(expenseRepository, times(1)).findById(expense1Id);
  }

  @Test
  void testFindByPaidBy() {
    when(expenseRepository.findByPaidBy(user1)).thenReturn(Arrays.asList(expense1));

    List<Expense> expenses = expenseService.findByPaidBy(user1);

    assertEquals(1, expenses.size());
    assertEquals("Groceries", expenses.get(0).getTitle());
    verify(expenseRepository, times(1)).findByPaidBy(user1);
  }

  @Test
  void testFindByUsers() {
    when(expenseRepository.findByUsers(Arrays.asList(user1, user2)))
        .thenReturn(Arrays.asList(expense1, expense2));

    List<Expense> expenses = expenseService.findByUsers(Arrays.asList(user1, user2));

    assertEquals(2, expenses.size());
    verify(expenseRepository, times(1)).findByUsers(Arrays.asList(user1, user2));
  }

  @Test
  void testFindByAmount() {
    when(expenseRepository.findByAmount(50.0)).thenReturn(Arrays.asList(expense1));

    List<Expense> expenses = expenseService.findByAmount(50.0);

    assertEquals(1, expenses.size());
    assertEquals("Groceries", expenses.get(0).getTitle());
    verify(expenseRepository, times(1)).findByAmount(50.0);
  }

  @Test
  void testFindByName() {
    when(expenseRepository.findByTitle("Groceries")).thenReturn(Arrays.asList(expense1));

    List<Expense> expenses = expenseService.findByTitle("Groceries");

    assertEquals(1, expenses.size());
    assertEquals("Groceries", expenses.get(0).getTitle());
    verify(expenseRepository, times(1)).findByTitle("Groceries");
  }

  @Test
  void testUpdateExpense() {
    when(expenseRepository.findById(expense1Id)).thenReturn(Optional.of(expense1));
    when(expenseRepository.save(any(Expense.class))).thenReturn(expense1);

    Expense updatedExpenseDetails = new Expense();
    updatedExpenseDetails.set_id(expense1Id);
    updatedExpenseDetails.setTitle("Updated Groceries");
    updatedExpenseDetails.setAmount(60.0);
    updatedExpenseDetails.setPaidBy(user1);
    updatedExpenseDetails.setUsers(Arrays.asList(user1, user2));

    Optional<Expense> updatedExpense = expenseService.updateExpense(expense1Id, updatedExpenseDetails);

    assertTrue(updatedExpense.isPresent());
    assertEquals("Updated Groceries", updatedExpense.get().getTitle());
    assertEquals(60.0, updatedExpense.get().getAmount());
    verify(expenseRepository, times(1)).findById(expense1Id);
    verify(expenseRepository, times(1)).save(expense1);
  }

  @Test
  void testDeleteExpense() {
    when(expenseRepository.findById(expense1Id)).thenReturn(Optional.of(expense1));
    doNothing().when(expenseRepository).delete(expense1);

    boolean isDeleted = expenseService.deleteExpense(expense1Id);

    assertTrue(isDeleted);
    verify(expenseRepository, times(1)).findById(expense1Id);
    verify(expenseRepository, times(1)).delete(expense1);
  }

  @Test
  void testDeleteExpense_NotFound() {
    ObjectId expense3Id = new ObjectId();
    when(expenseRepository.findById(expense3Id)).thenReturn(Optional.empty());

    boolean isDeleted = expenseService.deleteExpense(expense3Id);

    assertFalse(isDeleted);
    verify(expenseRepository, times(1)).findById(expense3Id);
    verify(expenseRepository, times(0)).delete(any(Expense.class));
  }
}
