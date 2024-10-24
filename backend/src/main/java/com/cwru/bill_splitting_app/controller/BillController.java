package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Bill>> getBillsById(@PathVariable String id) {
        List<Bill> bills = billService.getBillsByCustomId(id);
        if (bills.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(bills);
        }
    }

    @GetMapping("/expenses/{expenseId}")
    public ResponseEntity<Bill> getBillByExpenseId(@PathVariable String expenseId) {
        Optional<Bill> bill = billService.getBillByExpenseId(expenseId);
        return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Bill> getBillByUserId(@PathVariable String userId) {
        Optional<Bill> bill = billService.getBillByUserId(userId);
        return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill createdBill = billService.createBill(bill);
        return ResponseEntity.ok(createdBill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable String id, @RequestBody Bill billDetails) {
        Optional<Bill> updatedBill = billService.updateBill(id, billDetails);
        return updatedBill.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable String id) {
        boolean isDeleted = billService.deleteBill(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}