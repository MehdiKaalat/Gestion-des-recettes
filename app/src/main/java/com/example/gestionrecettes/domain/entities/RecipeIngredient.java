package com.example.gestionrecettes.domain.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import lombok.Data;

@Entity(tableName = "recipe_ingredients",
        foreignKeys = {
                @ForeignKey(entity = Recipe.class,
                        parentColumns = "recipeId",
                        childColumns = "recipeId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Ingredient.class,
                        parentColumns = "ingredientId",
                        childColumns = "ingredientId",
                        onDelete = ForeignKey.CASCADE)
        })
public class RecipeIngredient {
    @PrimaryKey(autoGenerate = true)
    public int recipeIngredientId;
    public int recipeId;
    public int ingredientId;
    public String quantity;

    public RecipeIngredient(int recipeId, int ingredientId, String quantity) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public void setRecipeIngredientId(int recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
