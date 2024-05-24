package com.example.gestionrecettes.presentation.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;

public class EditRecipeViewModelFactory implements ViewModelProvider.Factory {
    private final RecipeRepository recipeRepository;

    public EditRecipeViewModelFactory(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditRecipeViewModel.class)) {
            return (T) new EditRecipeViewModel(recipeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
