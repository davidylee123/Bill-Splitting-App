package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.service.BillService;
import org.bson.types.ObjectId;
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
  public ResponseEntity<Bill> getBillById(@PathVariable ObjectId id) {
    Optional<Bill> bill = billService.getBillById(id);
    return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/expenses/{expenseId}")
  public ResponseEntity<Bill> getBillByExpenseId(@PathVariable ObjectId expenseId) {
    Optional<Bill> bill = billService.getBillByExpenseId(expenseId);
    return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<Bill> getBillByUserId(@PathVariable ObjectId userId) {
    Optional<Bill> bill = billService.getBillByUserId(userId);
    return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
    Bill createdBill = billService.createBill(bill);
    return ResponseEntity.ok(createdBill);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Bill> updateBill(@PathVariable ObjectId id, @RequestBody Bill billDetails) {
    Optional<Bill> updatedBill = billService.updateBill(id, billDetails);
    return updatedBill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBill(@PathVariable ObjectId id) {
    boolean isDeleted = billService.deleteBill(id);
    return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
