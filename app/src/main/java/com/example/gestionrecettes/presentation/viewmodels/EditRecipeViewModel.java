package com.example.gestionrecettes.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionrecettes.data.DTOs.IngredientWithQuantity;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;

import java.util.List;

public class EditRecipeViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;

    public EditRecipeViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return recipeRepository.getRecipeById(recipeId);
    }

    public LiveData<List<IngredientWithQuantity>> getIngredientsForRecipe(int recipeId) {
        return recipeRepository.getIngredientsForRecipe(recipeId);
    }

    public void updateRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities) {
        recipeRepository.updateRecipeWithIngredients(recipe, ingredients, quantities);
    }
}
