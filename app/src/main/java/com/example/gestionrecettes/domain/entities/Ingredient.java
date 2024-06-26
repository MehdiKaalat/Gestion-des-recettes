package com.example.gestionrecettes.domain.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;

@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public int ingredientId;
    public String name;

    public Ingredient(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
