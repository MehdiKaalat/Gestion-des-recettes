package com.example.gestionrecettes.data.repositories.UserRepo;

import androidx.lifecycle.LiveData;

import com.example.gestionrecettes.domain.entities.User;

public interface UserRepository {
    LiveData<User> getUserById(int recipeId);
    LiveData<User> login(String email, String password);
    void register(byte[] imagePdp, String name, String email, String password);
    void updateUser(User user);
}
