package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String _id;
    private String userName;
    private String email;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public User() {}
}
