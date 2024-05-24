package com.example.gestionrecettes.presentation.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;

public class RecipeViewModelFactory implements ViewModelProvider.Factory {
    private final RecipeRepository recipeRepository;

    public RecipeViewModelFactory(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(recipeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
