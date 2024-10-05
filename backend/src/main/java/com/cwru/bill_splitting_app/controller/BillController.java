package com.cwru.bill_splitting_app.controller;

import com.cwru.bill_splitting_app.model.Bill;
import com.cwru.bill_splitting_app.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        return ResponseEntity.ok(billService.createBill(bill));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(billService.getBillsByUserId(userId));
    }
}
