package com.example.gestionrecettes.data.repositories;

import com.example.gestionrecettes.data.datasource.local.LocalDataSource;
import com.example.gestionrecettes.domain.entities.Recipe;
import java.util.List;

public class RecipeRepositoryImpl implements RecipeRepository {
    private final LocalDataSource localDataSource;

    public RecipeRepositoryImpl(LocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    @Override
    public void insertRecipe(Recipe recipe) {
        localDataSource.insertRecipe(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        localDataSource.updateRecipe(recipe);
    }

    @Override
    public void deleteRecipe(int recipeId) {
        localDataSource.deleteRecipe(recipeId);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return localDataSource.getAllRecipes();
    }

    @Override
    public Recipe getRecipeById(int recipeId) {
        return localDataSource.getRecipeById(recipeId);
    }
}
