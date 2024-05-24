package com.example.gestionrecettes.domain.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gestionrecettes.presentation.activities.profile_activity;

import lombok.Data;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;
    public String name;
    public String password;
    public String email;
    public byte[] pdp; // Byte array for storing image data


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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getPdp() {
        return pdp;
    }

    public void setPdp(byte[] pdp) {
        this.pdp = pdp;
    }

    public User(int userId, String name, String password, String email, byte[] pdp) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.pdp = pdp;
    }

    public User() {
    }
}
