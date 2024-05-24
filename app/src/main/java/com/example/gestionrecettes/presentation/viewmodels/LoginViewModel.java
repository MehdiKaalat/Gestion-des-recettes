package com.example.gestionrecettes.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.domain.entities.User;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<User> login(String email, String password) {
        return userRepository.login(email, password);
    }
}

