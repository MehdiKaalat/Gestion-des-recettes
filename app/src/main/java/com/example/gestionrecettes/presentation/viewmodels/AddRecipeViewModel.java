package com.example.gestionrecettes.presentation.viewmodels;

import androidx.lifecycle.ViewModel;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;

public class AddRecipeViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public AddRecipeViewModel(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }
    public void insertRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities) {
        recipeRepository.insertRecipeWithIngredients(recipe, ingredients, quantities);
    }
}
