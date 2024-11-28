package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "bills")
public class Bill {
    @Id
    private ObjectId _id;
    private String title;
    private List<Expense> expenses;
    private List<User> users;

    public Bill(ObjectId _id, String title, List<Expense> expenses, List<User> users) {
        this._id = _id;
        this.title = title;
        this.expenses = expenses;
        this.users = users;
    }

    public Bill(String title, List<Expense> expenses, List<User> users) {
        this._id = new ObjectId();  // Automatically generate ObjectId
        this.title = title;
        this.expenses = expenses;
        this.users = users;
    }

    public Bill() {
        this._id = new ObjectId();
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getId() {
        return this._id;
    }
}