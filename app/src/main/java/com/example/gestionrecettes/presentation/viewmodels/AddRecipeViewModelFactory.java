package com.example.gestionrecettes.presentation.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;

public class AddRecipeViewModelFactory implements ViewModelProvider.Factory {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public AddRecipeViewModelFactory(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddRecipeViewModel.class)) {
            return (T) new AddRecipeViewModel(recipeRepository, userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
