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
    // Getter for recipeDao
    public RecipeDao getRecipeDao() {
        return recipeDao;
    }
}