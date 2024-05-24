package com.example.gestionrecettes.data.DTOs;

public class IngredientWithQuantity {
    public String name;
    public String quantity;

    public IngredientWithQuantity(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
