package com.example.gestionrecettes.data.datasource.local;

import android.content.Context;

import androidx.room.Room;

import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.framework.database.AppDatabase;


import java.util.List;

public class LocalDataSource {
    private final RecipeDao recipeDao;


    public LocalDataSource(Context context) {
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
