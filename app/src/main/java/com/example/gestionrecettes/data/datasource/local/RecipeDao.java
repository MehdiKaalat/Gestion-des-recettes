package com.example.gestionrecettes.data.datasource.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.gestionrecettes.domain.entities.Recipe;
import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("DELETE FROM recipes WHERE recipeId = :recipeId")
    void deleteById(int recipeId);

    @Query("SELECT * FROM recipes")
    List<Recipe> getAll();

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    Recipe getById(int recipeId);
}
