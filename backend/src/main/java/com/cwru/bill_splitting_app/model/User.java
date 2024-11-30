package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
  @Id
  private ObjectId _id;
  private String name;
  private String email;
  private List<ObjectId> friends;

  // public User(String name, String email) {
  //   this.id = id;
  //   this.name = name;
  //   this.email = email;
  // }

  // public User() {
  // }
}
