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

        Expense expense1 = new Expense("exp1", "Groceries", 50.0, "David Lee", Arrays.asList("David Lee", "Jose Kim"));
        Expense expense2 = new Expense("exp2", "Electricity", 75.5, "Jose Kim", Arrays.asList("David Lee", "Jose Kim"));

        User user1 = new User("user1", "David Lee", "david@example.com");
        User user2 = new User("user2", "Jose Kim", "jose@example.com");

        bill = new Bill("1", "Household Bill", Arrays.asList(expense1, expense2), Arrays.asList(user1, user2));

        updatedBill = new Bill("1", "Updated Household Bill", Arrays.asList(expense1), Arrays.asList(user1));
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
        when(billRepository.findByCustomId(bill.getId())).thenReturn(Optional.empty());
        when(billRepository.save(bill)).thenReturn(bill);

        Bill createdBill = billService.createBill(bill);
        assertNotNull(createdBill);
        assertEquals(bill.getId(), createdBill.getId());
        verify(billRepository, times(1)).findByCustomId(bill.getId());
        verify(billRepository, times(1)).save(bill);
    }

    @Test
    void createBill_ThrowsExceptionIfBillExists() {
        when(billRepository.findByCustomId(bill.getId())).thenReturn(Optional.of(bill));

        assertThrows(IllegalArgumentException.class, () -> billService.createBill(bill));
        verify(billRepository, times(1)).findByCustomId(bill.getId());
        verify(billRepository, never()).save(any(Bill.class));
    }

    @Test
    void getBillsByCustomId() {
        when(billRepository.findAllById(bill.getId())).thenReturn(Arrays.asList(bill));

        List<Bill> bills = billService.getBillsByCustomId(bill.getId());
        assertNotNull(bills);
        assertEquals(1, bills.size());
        assertEquals(bill.getId(), bills.get(0).getId());
        verify(billRepository, times(1)).findAllById(bill.getId());
    }

    @Test
    void getBillByExpenseId() {
        when(billRepository.findByExpensesId("exp1")).thenReturn(Optional.of(bill));

        Optional<Bill> result = billService.getBillByExpenseId("exp1");
        assertTrue(result.isPresent());
        assertEquals(bill.getId(), result.get().getId());
        verify(billRepository, times(1)).findByExpensesId("exp1");
    }

    @Test
    void getBillByUserId() {
        when(billRepository.findByUsersId("user1")).thenReturn(Optional.of(bill));

        Optional<Bill> result = billService.getBillByUserId("user1");
        assertTrue(result.isPresent());
        assertEquals(bill.getId(), result.get().getId());
        verify(billRepository, times(1)).findByUsersId("user1");
    }

    @Test
    void updateBill() {
        when(billRepository.findByCustomId(bill.getId())).thenReturn(Optional.of(bill));
        when(billRepository.save(any(Bill.class))).thenReturn(updatedBill);

        Optional<Bill> result = billService.updateBill(bill.getId(), updatedBill);
        assertTrue(result.isPresent());
        assertEquals("Updated Household Bill", result.get().getTitle());
        verify(billRepository, times(1)).findByCustomId(bill.getId());
        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    void updateBill_NotFound() {
        when(billRepository.findByCustomId(bill.getId())).thenReturn(Optional.empty());

        Optional<Bill> result = billService.updateBill(bill.getId(), updatedBill);
        assertFalse(result.isPresent());
        verify(billRepository, times(1)).findByCustomId(bill.getId());
        verify(billRepository, never()).save(any(Bill.class));
    }

    @Test
    void deleteBill() {
        when(billRepository.findByCustomId(bill.getId())).thenReturn(Optional.of(bill));
        doNothing().when(billRepository).delete(bill);

        boolean result = billService.deleteBill(bill.getId());
        assertTrue(result);
        verify(billRepository, times(1)).findByCustomId(bill.getId());
        verify(billRepository, times(1)).delete(bill);
    }

    @Test
    void deleteBill_NotFound() {
        when(billRepository.findByCustomId(bill.getId())).thenReturn(Optional.empty());

        boolean result = billService.deleteBill(bill.getId());
        assertFalse(result);
        verify(billRepository, times(1)).findByCustomId(bill.getId());
        verify(billRepository, never()).delete(any(Bill.class));
    }
}