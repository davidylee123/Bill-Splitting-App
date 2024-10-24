package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;
    private String name;
    private double amount;
    private String paidBy;
    private List<String> splitBetween;
}
