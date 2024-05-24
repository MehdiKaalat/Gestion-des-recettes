package com.example.gestionrecettes.presentation.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;

public class SignupViewModelFactory implements ViewModelProvider.Factory{
    private final UserRepository userRepository;

    public SignupViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            return (T) new SignupViewModel(userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
