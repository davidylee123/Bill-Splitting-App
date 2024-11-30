package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Document(collection = "expenses")
public class Expense {
  @Id
  private ObjectId _id;
  private String title;
  private double amount;
  private ObjectId paidBy;
  private List<ObjectId> users;

  // public Expense(ObjectId _id, String title, double amount, ObjectId paidBy, List<ObjectId> users) {
  //   this._id = _id;
  //   this.title = title;
  //   this.amount = amount;
  //   this.paidBy = paidBy;
  //   this.users = users;
  // }

  // public Expense() {
  // }
}
