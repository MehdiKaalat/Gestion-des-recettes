package com.example.gestionrecettes.data.databaseClasses;

public class LoggedInUser {

    private String userId;

    public LoggedInUser(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

}