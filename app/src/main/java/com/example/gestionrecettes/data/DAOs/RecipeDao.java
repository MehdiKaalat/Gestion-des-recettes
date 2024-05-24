package com.example.gestionrecettes.data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gestionrecettes.data.DTOs.IngredientWithQuantity;
import com.example.gestionrecettes.data.DTOs.RecipeWithUserNameDTO;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.domain.entities.RecipeIngredient;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    long insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Query("DELETE FROM recipes WHERE recipeId = :recipeId")
    void deleteById(int recipeId);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getAll();

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    LiveData<Recipe> getById(int recipeId);

    @Query("SELECT recipes.*, users.name AS userName FROM recipes JOIN users ON recipes.userId = users.userId")
    LiveData<List<RecipeWithUserNameDTO>> getAllRecipesWithUserNames();

    @Query("SELECT recipes.*, users.name AS userName FROM recipes JOIN users ON recipes.userId = users.userId WHERE recipes.userId = :userId")
    LiveData<List<RecipeWithUserNameDTO>> getRecipesForUser(int userId);
    @Query("SELECT COUNT(*) FROM recipes WHERE userId = :userId")
    LiveData<Integer> getRecipeCountByUserId(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIngredient(Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecipeIngredient(RecipeIngredient recipeIngredient);

    @Transaction
    default void insertRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities) {
        long recipeId = insert(recipe);
        for (int i = 0; i < ingredients.length; i++) {
            long ingredientId = insertIngredient(ingredients[i]);
            insertRecipeIngredient(new RecipeIngredient((int) recipeId, (int) ingredientId, quantities[i]));
        }
    }
    @Query("SELECT ingredients.name, recipe_ingredients.quantity " +
            "FROM ingredients " +
            "JOIN recipe_ingredients ON ingredients.ingredientId = recipe_ingredients.ingredientId " +
            "WHERE recipe_ingredients.recipeId = :recipeId")
    LiveData<List<IngredientWithQuantity>> getIngredientsForRecipe(int recipeId);

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    LiveData<Recipe> getRecipeByIdLive(int recipeId);

    @Transaction
    default void updateRecipeWithIngredients(Recipe recipe, Ingredient[] ingredients, String[] quantities) {
        update(recipe);
        deleteIngredientsByRecipeId(recipe.getRecipeId());
        for (int i = 0; i < ingredients.length; i++) {
            long ingredientId = insertIngredient(ingredients[i]);
            insertRecipeIngredient(new RecipeIngredient(recipe.getRecipeId(), (int) ingredientId, quantities[i]));
        }
    }
    @Query("DELETE FROM recipe_ingredients WHERE recipeId = :recipeId")
    void deleteIngredientsByRecipeId(int recipeId);

}
