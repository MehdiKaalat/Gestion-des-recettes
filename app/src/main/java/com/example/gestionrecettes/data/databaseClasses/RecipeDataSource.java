package com.example.gestionrecettes.data.databaseClasses;

import android.content.Context;

import androidx.room.Room;

import com.example.gestionrecettes.data.DAOs.RecipeDao;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.framework.database.AppDatabase;


import java.util.List;

public class RecipeDataSource {
    private final RecipeDao recipeDao;


    public RecipeDataSource(Context context) {
        // Initialize the Room database and obtain the DAO
        AppDatabase appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "myDB")
                .build();
        recipeDao = appDatabase.recipeDao();
    }


    public void insertRecipe(Recipe recipe) {
        recipeDao.insert(recipe);
    }

    public void updateRecipe(Recipe recipe) {
        recipeDao.update(recipe);
    }

    public void deleteRecipe(int recipeId) {
        recipeDao.deleteById(recipeId);
    }

    public List<Recipe> getAllRecipes() {
        return recipeDao.getAll();
    }

    public Recipe getRecipeById(int recipeId) {
        return recipeDao.getById(recipeId);
    }
}
