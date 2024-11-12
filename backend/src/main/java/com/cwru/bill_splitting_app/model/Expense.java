package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "expenses")
public class Expense {
    @Id
    private String _id;
    private String id;
    private String name;
    private double amount;
    private String paidBy;
    private List<String> splitBetween;

    public Expense(String id, String name, double amount, String paidBy, List<String> splitBetween) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitBetween = splitBetween;
    }

    public Expense() {}
}