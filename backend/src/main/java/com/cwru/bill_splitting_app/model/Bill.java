package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "bills")
public class Bill {
    @Id
    private String id;
    private String title;
    private List<Expense> expenses;
    private List<User> users;

    public Bill(String id, String title, List<Expense> expenses, List<User> users) {
        this.id = id;
        this.title = title;
        this.expenses = expenses;
        this.users = users;
    }

    public Bill() {}
}