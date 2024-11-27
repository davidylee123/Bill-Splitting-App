package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String _id;
    private String id;
    private String userName;
    private String email;
    private String passwordHash;
    private List<String> friends;

    public User(String id, String userName, String email, String passwordHash, List<String> friends) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.friends = friends == null ? new ArrayList<>() : friends;
    }

    public User(String id, String userName, String email) {
        this(id, userName, email, null, null);
    }

    public User() {
        this.friends = new ArrayList<>();
    }
}