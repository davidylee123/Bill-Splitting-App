package com.cwru.bill_splitting_app.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private ObjectId _id;
    private String name;
    private String email;
    private String password;
    private List<ObjectId> friends;

    public User(String name, String email, String password) {
        this._id = new ObjectId();
        this.name = name;
        this.email = email;
        this.password = password;
        this.friends = new ArrayList<>();
    }

    public User(ObjectId _id, String name, String email, String password, List<ObjectId> friends) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.friends = friends != null ? friends : new ArrayList<>();
    }

    public User(ObjectId _id, String name, String email, String password) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.friends = new ArrayList<>();
    }

    public User() {
        this._id = new ObjectId();
        this.friends = new ArrayList<>();
    }


}