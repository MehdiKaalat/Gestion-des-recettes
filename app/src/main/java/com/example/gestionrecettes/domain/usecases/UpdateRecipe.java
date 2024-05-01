package com.example.gestionrecettes.domain.usecases;

import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.data.repositories.RecipeRepositoryImpl;

public class UpdateRecipe {
    private final RecipeRepositoryImpl recipeRepository;

    public UpdateRecipe(RecipeRepositoryImpl recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void execute(Recipe recipe) {
        // Update the recipe in the repository
        recipeRepository.updateRecipe(recipe);
    }
}
