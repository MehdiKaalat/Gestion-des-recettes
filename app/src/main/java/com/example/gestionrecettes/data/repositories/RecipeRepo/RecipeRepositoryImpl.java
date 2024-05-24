package com.example.gestionrecettes.data.repositories.RecipeRepo;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.gestionrecettes.data.DAOs.RecipeDao;
import com.example.gestionrecettes.data.DTOs.IngredientWithQuantity;
import com.example.gestionrecettes.data.DTOs.RecipeWithUserNameDTO;
import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeRepositoryImpl implements RecipeRepository {
    private RecipeDao recipeDao;
    private final ExecutorService executorService;

    public RecipeRepositoryImpl(RecipeDataSource recipeDataSource) {
        this.recipeDao = recipeDataSource.getRecipeDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void deleteRecipe(int recipeId) {
        executorService.execute(() -> recipeDao.deleteById(recipeId));
    }


    @Override
    public LiveData<Recipe> getRecipeById(int recipeId) {
        return recipeDao.getById(recipeId);
    }

    @Override
    public LiveData<List<RecipeWithUserNameDTO>> getRecipesWithUserDetails() {
        return recipeDao.getAllRecipesWithUserNames();
    }

    @Override
    public LiveData<List<RecipeWithUserNameDTO>> getRecipesForUser(int userId) {
        return recipeDao.getRecipesForUser(userId);
    }
    public LiveData<Integer> getRecipeCountByUserId(int userId) {
        return recipeDao.getRecipeCountByUserId(userId);  // Implement this query in your RecipeDao
    }
    @Override
    public void insertRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities) {
        executorService.execute(() -> recipeDao.insertRecipeWithIngredients(recipe, ingredients, quantities));
    }
    public LiveData<List<IngredientWithQuantity>> getIngredientsForRecipe(int recipeId) {
        return recipeDao.getIngredientsForRecipe(recipeId);
    }
    @Override
    public LiveData<Recipe> getRecipeByIdLive(int recipeId) {
        return recipeDao.getRecipeByIdLive(recipeId);
    }
    @Override
    public void updateRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities) {
        executorService.execute(() -> recipeDao.updateRecipeWithIngredients(recipe, ingredients, quantities));
    }

}

