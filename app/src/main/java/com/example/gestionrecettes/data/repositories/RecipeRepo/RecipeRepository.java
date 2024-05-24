package com.example.gestionrecettes.data.repositories.RecipeRepo;

import androidx.lifecycle.LiveData;

import com.example.gestionrecettes.data.DTOs.IngredientWithQuantity;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.data.DTOs.RecipeWithUserNameDTO;
import java.util.List;

public interface RecipeRepository {
    void deleteRecipe(int recipeId);
    LiveData<Recipe> getRecipeById(int recipeId);

    LiveData<List<RecipeWithUserNameDTO>> getRecipesWithUserDetails();
    LiveData<List<RecipeWithUserNameDTO>> getRecipesForUser(int userId);
    LiveData<Integer> getRecipeCountByUserId(int userId);
    void insertRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities);
    LiveData<List<IngredientWithQuantity>> getIngredientsForRecipe(int recipeId);
    LiveData<Recipe> getRecipeByIdLive(int recipeId);

    void updateRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities);
}
