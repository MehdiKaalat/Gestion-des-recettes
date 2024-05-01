package com.example.gestionrecettes.domain.usecases;

import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.data.repositories.RecipeRepositoryImpl;

public class AddRecipe {
    private final RecipeRepositoryImpl recipeRepository;

    public AddRecipe(RecipeRepositoryImpl recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void execute(Recipe recipe) {
        // Add the recipe to the repository
        recipeRepository.insertRecipe(recipe);
    }
}
