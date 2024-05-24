package com.example.gestionrecettes.presentation.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private RecipeRepository recipeRepository;
    private UserRepository userRepository;
    private int userId;

    public ProfileViewModelFactory(RecipeRepository recipeRepository,UserRepository userRepository,  int userId) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.userId = userId;
    }

    public ProfileViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(recipeRepository,userRepository, userId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}