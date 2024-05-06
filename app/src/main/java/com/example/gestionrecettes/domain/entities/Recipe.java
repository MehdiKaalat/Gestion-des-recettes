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
                        onDelete = ForeignKey.CASCADE)
        })
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int recipeId;
    public int userId; // Foreign key to 'users' table
    public String title;
    public String description;
    public String category;
    public byte[] image; // Byte array for storing image data
    public int cookingTime; // In minutes



    public Recipe(int userId, String title, String description, byte[] image, int cookingTime, String category) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.cookingTime = cookingTime;
        this.category = category;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
