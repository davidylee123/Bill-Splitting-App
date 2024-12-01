package com.cwru.bill_splitting_app.model;

import com.cwru.bill_splitting_app.util.ObjectIdSerializer;
import com.cwru.bill_splitting_app.util.ObjectIdDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Document(collection = "bills")
public class Bill {

  @Id
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId _id;
  private String title;
  private List<Expense> expenses;
  private List<User> users;
}