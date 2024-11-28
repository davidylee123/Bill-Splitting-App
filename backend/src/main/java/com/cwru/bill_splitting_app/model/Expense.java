package com.cwru.bill_splitting_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "expenses")
public class Expense {

    @Id
    @JsonProperty("_id")
    private ObjectId _id;
    private String name;
    private double amount;
    private String paidBy;
    private List<String> splitBetween;

    public Expense(ObjectId _id, String name, double amount, String paidBy, List<String> splitBetween) {
        this._id = _id;
        this.name = name;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitBetween = splitBetween;
    }

    public Expense() {
        this._id = new ObjectId();
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId get_id() {
        return _id;
    }
}