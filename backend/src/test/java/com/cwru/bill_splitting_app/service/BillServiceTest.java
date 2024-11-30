package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.BillRepository;
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

class BillServiceTest {

  @Mock
  private BillRepository billRepository;

  @InjectMocks
  private BillService billService;

  private ObjectId billId;
  private ObjectId updatedBillId;
  private Bill bill;
  private Bill updatedBill;

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

    bill = new Bill();
    billId = new ObjectId();
    bill.set_id(billId);
    bill.setTitle("Sample Bill");
    bill.setExpenses(Arrays.asList(expense1));
    bill.setUsers(Arrays.asList(user1, user2));

    updatedBill = new Bill();
    updatedBillId = new ObjectId();
    updatedBill.set_id(updatedBillId);
    updatedBill.setTitle("Updated Household Bill");
    updatedBill.setExpenses(Arrays.asList(expense2));
    updatedBill.setUsers(Arrays.asList(user1));
  }

  @Test
  void getAllBills() {
    when(billRepository.findAll()).thenReturn(Arrays.asList(bill));

    List<Bill> bills = billService.getAllBills();
    assertNotNull(bills);
    assertEquals(1, bills.size());
    verify(billRepository, times(1)).findAll();
  }

  @Test
  void createBill() {
    when(billRepository.findById(bill.get_id())).thenReturn(Optional.empty());
    when(billRepository.save(bill)).thenReturn(bill);

    Bill createdBill = billService.createBill(bill);
    assertNotNull(createdBill);
    assertEquals(bill.get_id(), createdBill.get_id());
    verify(billRepository, times(1)).findById(bill.get_id());
    verify(billRepository, times(1)).save(bill);
  }

  @Test
  void createBill_ThrowsExceptionIfBillExists() {
    when(billRepository.findById(bill.get_id())).thenReturn(Optional.of(bill));

    assertThrows(IllegalArgumentException.class, () -> billService.createBill(bill));
    verify(billRepository, times(1)).findById(bill.get_id());
    verify(billRepository, never()).save(any(Bill.class));
  }

  @Test
  void getBillsById() {
    when(billRepository.findById(bill.get_id())).thenReturn(Optional.of(bill));

    Optional<Bill> foundBill = billService.getBillById(bill.get_id());
    assertTrue(foundBill.isPresent());
    assertEquals(bill.get_id(), foundBill.get().get_id());
    verify(billRepository, times(1)).findById(bill.get_id());
  }

//  @Test
//  void getBillByExpenseId() {
//    when(billRepository.findById(expense1Id)).thenReturn(Optional.of(bill));
//
//    Optional<Bill> result = billService.getBillByExpenseId(expense1Id);
//    assertTrue(result.isPresent());
//    assertEquals(bill.get_id(), result.get().get_id());
//    verify(billRepository, times(1)).findByExpensesId(expense1Id);
//  }

  @Test
  void getBillByUserId() {
    when(billRepository.findByUsersId(user1Id)).thenReturn(Optional.of(bill));

    Optional<Bill> result = billService.getBillByUserId(user1Id);
    assertTrue(result.isPresent());
    assertEquals(bill.get_id(), result.get().get_id());
    verify(billRepository, times(1)).findByUsersId(user1Id);
  }

  @Test
  void updateBill() {
    when(billRepository.findById(bill.get_id())).thenReturn(Optional.of(bill));
    when(billRepository.save(any(Bill.class))).thenReturn(updatedBill);

    Optional<Bill> result = billService.updateBill(bill.get_id(), updatedBill);
    assertTrue(result.isPresent());
    assertEquals("Updated Household Bill", result.get().getTitle());
    verify(billRepository, times(1)).findById(bill.get_id());
    verify(billRepository, times(1)).save(any(Bill.class));
  }

  @Test
  void updateBill_NotFound() {
    when(billRepository.findById(bill.get_id())).thenReturn(Optional.empty());

    Optional<Bill> result = billService.updateBill(bill.get_id(), updatedBill);
    assertFalse(result.isPresent());
    verify(billRepository, times(1)).findById(bill.get_id());
    verify(billRepository, never()).save(any(Bill.class));
  }

  @Test
  void deleteBill() {
    when(billRepository.findById(bill.get_id())).thenReturn(Optional.of(bill));
    doNothing().when(billRepository).delete(bill);

    boolean result = billService.deleteBill(bill.get_id());
    assertTrue(result);
    verify(billRepository, times(1)).findById(bill.get_id());
    verify(billRepository, times(1)).delete(bill);
  }

  @Test
  void deleteBill_NotFound() {
    when(billRepository.findById(bill.get_id())).thenReturn(Optional.empty());

    boolean result = billService.deleteBill(bill.get_id());
    assertFalse(result);
    verify(billRepository, times(1)).findById(bill.get_id());
    verify(billRepository, never()).delete(any(Bill.class));
  }
}
