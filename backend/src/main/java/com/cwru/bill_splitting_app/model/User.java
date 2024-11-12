package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String _id;
    private String id;
    private String userName;
    private String email;

    public User(String id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public User() {}
}
