package com.example.gestionrecettes.domain.usecases.RecipeUseCases;

import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;
import java.util.List;

public class GetRecipes {
    private final RecipeRepositoryImpl recipeRepository;

    public GetRecipes(RecipeRepositoryImpl recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> execute() {
        // Retrieve all recipes from the repository
        return recipeRepository.getAllRecipes();
    }
}
