package com.cwru.bill_splitting_app.service;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.model.Expense;
import com.cwru.bill_splitting_app.model.User;
import com.cwru.bill_splitting_app.repository.BillRepository;
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

class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    private Bill bill;
    private Bill updatedBill;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Expense expense1 = new Expense(new ObjectId(), "Groceries", 50.0, "David Lee", Arrays.asList("David Lee", "Jose Kim"));
        Expense expense2 = new Expense(new ObjectId(), "Electricity", 75.5, "Jose Kim", Arrays.asList("David Lee", "Jose Kim"));

        User user1 = new User(new ObjectId(), "David Lee", "david@example.com", "password123", Arrays.asList(new ObjectId()));
        User user2 = new User(new ObjectId(), "Jose Kim", "jose@example.com", "password456", Arrays.asList(new ObjectId()));

        bill = new Bill(new ObjectId(), "Household Bill", Arrays.asList(expense1, expense2), Arrays.asList(user1, user2));

        updatedBill = new Bill(new ObjectId(), "Updated Household Bill", Arrays.asList(expense1), Arrays.asList(user1));
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

        Optional<Bill> result = billService.getBillById(bill.get_id());
        assertTrue(result.isPresent());
        assertEquals(bill.get_id(), result.get().get_id());
        verify(billRepository, times(1)).findById(bill.get_id());
    }

    @Test
    void getBillByExpenseId() {
        ObjectId expenseId = new ObjectId();
        when(billRepository.findByExpensesId(expenseId)).thenReturn(Optional.of(bill));

        Optional<Bill> result = billService.getBillByExpenseId(expenseId);
        assertTrue(result.isPresent());
        assertEquals(bill.get_id(), result.get().get_id());
        verify(billRepository, times(1)).findByExpensesId(expenseId);
    }

    @Test
    void getBillByUserId() {
        ObjectId userId = new ObjectId();
        when(billRepository.findByUsersId(userId)).thenReturn(Optional.of(bill));

        Optional<Bill> result = billService.getBillByUserId(userId);
        assertTrue(result.isPresent());
        assertEquals(bill.get_id(), result.get().get_id());
        verify(billRepository, times(1)).findByUsersId(userId);
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