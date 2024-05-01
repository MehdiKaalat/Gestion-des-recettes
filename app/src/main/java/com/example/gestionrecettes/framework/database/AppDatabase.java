package com.example.gestionrecettes.framework.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gestionrecettes.data.datasource.local.CategoryDao;
import com.example.gestionrecettes.data.datasource.local.IngredientDao;
import com.example.gestionrecettes.data.datasource.local.RecipeDao;
import com.example.gestionrecettes.data.datasource.local.RecipeIngredientDao;
import com.example.gestionrecettes.data.datasource.local.UserDao;
import com.example.gestionrecettes.domain.entities.Category;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.domain.entities.RecipeIngredient;
import com.example.gestionrecettes.domain.entities.User;

@Database(entities = {Recipe.class, Category.class, Ingredient.class, RecipeIngredient.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;
    public abstract RecipeDao recipeDao();
    public abstract CategoryDao categoryDao();
    public abstract IngredientDao ingredientDao();
    public abstract RecipeIngredientDao recipeIngredientDao();
    public abstract UserDao userDao();

    public synchronized static AppDatabase getInstance(Context context){
        if (database == null){
            String DATABASE_NAME = "myDB";
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
