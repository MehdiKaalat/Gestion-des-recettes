package com.example.gestionrecettes.data.repositories.RecipeRepo;

import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;
import com.example.gestionrecettes.domain.entities.Recipe;
import java.util.List;

public class RecipeRepositoryImpl implements RecipeRepository {
    private final RecipeDataSource recipeDataSource;

    public RecipeRepositoryImpl(RecipeDataSource recipeDataSource) {
        this.recipeDataSource = recipeDataSource;
    }

    @Override
    public void insertRecipe(Recipe recipe) {
        recipeDataSource.insertRecipe(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipeDataSource.updateRecipe(recipe);
    }

    @Override
    public void deleteRecipe(int recipeId) {
        recipeDataSource.deleteRecipe(recipeId);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeDataSource.getAllRecipes();
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        return recipeDataSource.getRecipeById(recipeId);
    }
}
