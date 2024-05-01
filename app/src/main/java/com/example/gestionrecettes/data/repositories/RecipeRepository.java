package com.example.gestionrecettes.data.repositories;

import com.example.gestionrecettes.domain.entities.Recipe;
import java.util.List;

public interface RecipeRepository {
    void insertRecipe(Recipe recipe);
    void updateRecipe(Recipe recipe);
    void deleteRecipe(int recipeId);
    List<Recipe> getAllRecipes();
    Recipe getRecipeById(int recipeId);
}
