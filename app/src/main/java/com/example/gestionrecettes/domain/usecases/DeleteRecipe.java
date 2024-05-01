package com.example.gestionrecettes.domain.usecases;

import com.example.gestionrecettes.data.repositories.RecipeRepositoryImpl;

public class DeleteRecipe {
    private final RecipeRepositoryImpl recipeRepository;

    public DeleteRecipe(RecipeRepositoryImpl recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void execute(int recipeId) {
        // Delete the recipe from the repository
        recipeRepository.deleteRecipe(recipeId);
    }
}
