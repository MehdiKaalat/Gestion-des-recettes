package com.example.gestionrecettes.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.DTOs.IngredientWithQuantity;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private final List<IngredientWithQuantity> ingredients;

    public IngredientAdapter(List<IngredientWithQuantity> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientWithQuantity ingredient = ingredients.get(position);
        holder.ingredientName.setText(ingredient.name);
        holder.ingredientQuantity.setText(ingredient.quantity + "g");
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;
        TextView ingredientQuantity;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}
