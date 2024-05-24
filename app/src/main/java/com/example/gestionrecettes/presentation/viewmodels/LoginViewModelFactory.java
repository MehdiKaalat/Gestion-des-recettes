package com.example.gestionrecettes.presentation.viewmodels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.data.databaseClasses.LoginRepository;
import com.example.gestionrecettes.data.databaseClasses.UserDataSource;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepositoryImpl;

public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private final UserRepository userRepository;

    public LoginViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

