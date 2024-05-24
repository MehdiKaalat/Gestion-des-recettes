package com.example.gestionrecettes.presentation.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.DTOs.RecipeWithUserNameDTO;
import com.example.gestionrecettes.presentation.activities.activity_recipe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> implements Filterable {
    private static final int PROFILE_ACTIVITY_REQUEST_CODE = 1;
    private List<RecipeWithUserNameDTO> recipes;
    private final Context context;
    private int signedInUserId;
    private final List<RecipeWithUserNameDTO> recipesFull;
    private String currentCategory = "All";

    public RecipeAdapter(Context context, List<RecipeWithUserNameDTO> recipes, int signedInUserId) {
        this.context = context;
        this.recipes = recipes;
        this.signedInUserId = signedInUserId;
        this.recipesFull = new ArrayList<>(recipes);
    }
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_item_layout, parent, false);
        return new RecipeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeWithUserNameDTO recipe = recipes.get(position);
        holder.recipeTitle.setText(recipe.getTitle());
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length);
        holder.recipeImage.setImageBitmap(imageBitmap);
        holder.categoryTitle.setText(recipe.getCategory());
        holder.byName.setText("By " + recipe.getUserName());
        // Set up a click listener on the itemView
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, activity_recipe.class);
            intent.putExtra("RECIPE_ID", recipe.getRecipeId());
            intent.putExtra("USER_NAME", recipe.getUserName());
            intent.putExtra("RECIPE_TITLE", recipe.getTitle());
            intent.putExtra("RECIPE_IMAGE", recipe.getImage());
            intent.putExtra("RECIPE_DESCRIPTION", recipe.getDescription());
            intent.putExtra("RECIPE_CATEGORY", recipe.getCategory());
            intent.putExtra("COOKING_TIME", recipe.getCookingTime());
            intent.putExtra("RECIPE_OWNER_ID", recipe.getUserId());
            intent.putExtra("SIGNED_IN_USER_ID", signedInUserId);

            ((Activity) context).startActivityForResult(intent, PROFILE_ACTIVITY_REQUEST_CODE);
        });

    }
    @Override
    public int getItemCount() {
        return recipes.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateRecipes(List<RecipeWithUserNameDTO> newRecipes) {
        this.recipes = newRecipes;
        notifyDataSetChanged();
    }


    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle;
        TextView categoryTitle;
        TextView byName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            categoryTitle = itemView.findViewById(R.id.category_title);
            byName = itemView.findViewById(R.id.byName);
        }
    }
    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    public void setCurrentCategory(String category) {
        this.currentCategory = category;
        getFilter().filter("");
    }

    private Filter recipeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RecipeWithUserNameDTO> filteredList = new ArrayList<>();
            for (RecipeWithUserNameDTO item : recipesFull) {
                if ((currentCategory.equals("All") || item.getCategory().equalsIgnoreCase(currentCategory)) &&
                        item.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filteredList.add(item);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipes.clear();
            if (results.values != null) {
                recipes.addAll((List) results.values);
            }
            notifyDataSetChanged();
        }
    };

}

