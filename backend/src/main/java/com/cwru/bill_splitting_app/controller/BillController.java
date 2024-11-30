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
  public ResponseEntity<Bill> getBillById(@PathVariable String id) {
    Optional<Bill> bill = billService.getBillById(new ObjectId(id));
    return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/expenses/{expenseId}")
  public ResponseEntity<Bill> getBillByExpenseId(@PathVariable String expenseId) {
    Optional<Bill> bill = billService.getBillByExpenseId(new ObjectId(expenseId));
    return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<Bill> getBillByUserId(@PathVariable String userId) {
    Optional<Bill> bill = billService.getBillByUserId(new ObjectId(userId));
    return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
    Bill createdBill = billService.createBill(bill);
    return ResponseEntity.ok(createdBill);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Bill> updateBill(@PathVariable String id, @RequestBody Bill billDetails) {
    Optional<Bill> updatedBill = billService.updateBill(new ObjectId(id), billDetails);
    return updatedBill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBill(@PathVariable String id) {
    boolean isDeleted = billService.deleteBill(new ObjectId(id));
    return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
