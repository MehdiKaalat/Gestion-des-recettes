package com.example.gestionrecettes.domain.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String name;
    public String password;
    public String email;


    public int getUserId() {
        return userId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
