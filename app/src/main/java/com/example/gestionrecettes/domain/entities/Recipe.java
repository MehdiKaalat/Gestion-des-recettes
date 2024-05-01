package com.example.gestionrecettes.domain.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import lombok.Data;

@Entity(tableName = "recipes",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Category.class,
                        parentColumns = "categoryId",
                        childColumns = "categoryId",
                        onDelete = ForeignKey.SET_NULL)
        })
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int recipeId;
    public int userId; // Foreign key to 'users' table
    public String title;
    public String description;
    public String imageUrl;
    public int cookingTime; // In minutes
    public Integer categoryId; // Foreign key to 'categories' table, allows null if category is deleted



    public Recipe(int recipeId, int userId, String title, String description, String imageUrl, int cookingTime, Integer categoryId) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.cookingTime = cookingTime;
        this.categoryId = categoryId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
