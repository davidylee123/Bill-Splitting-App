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
  private User paidBy;
  private List<User> users;
}
