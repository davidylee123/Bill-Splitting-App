package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Document(collection = "bills")
public class Bill {
  @Id
  private ObjectId _id;
  private String title;
  private List<Expense> expenses;
  private List<ObjectId> users;

  // public Bill(ObjectId _id, String title, List<Expense> expenses, List<ObjectId> users) {
  //   this._id = _id;
  //   this.title = title;
  //   this.expenses = expenses;
  //   this.users = users;
  // }

  // public Bill() {
  // }
}
