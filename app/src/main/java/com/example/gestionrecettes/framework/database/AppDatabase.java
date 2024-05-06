package com.example.gestionrecettes.framework.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gestionrecettes.data.DAOs.RecipeDao;
import com.example.gestionrecettes.data.DAOs.UserDao;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.domain.entities.User;

@Database(entities = {Recipe.class, User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;
    public abstract RecipeDao recipeDao();
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
