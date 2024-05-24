package com.example.gestionrecettes.presentation.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.DTOs.IngredientWithQuantity;
import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.presentation.viewmodels.EditRecipeViewModel;
import com.example.gestionrecettes.presentation.viewmodels.EditRecipeViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class edit_recipe_activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextTitle, editTextDescription, editTextCookingTime;
    private ImageView imageViewRecipe;
    private Spinner spinnerCategory;
    private LinearLayout ingredientsContainer;
    private EditRecipeViewModel editRecipeViewModel;
    private List<EditText> ingredientInputs;
    private List<EditText> quantityInputs;
    private int recipeId;
    private Recipe recipe;
    private LinearLayout initialIngredientRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCookingTime = findViewById(R.id.editTextCookingTime);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        Button buttonAddIngredient = findViewById(R.id.buttonAddIngredient);
        Button buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);

        ingredientInputs = new ArrayList<>();
        quantityInputs = new ArrayList<>();

        initialIngredientRow = (LinearLayout) ingredientsContainer.getChildAt(0);

        RecipeRepositoryImpl recipeRepository = new RecipeRepositoryImpl(new RecipeDataSource(this));
        EditRecipeViewModelFactory factory = new EditRecipeViewModelFactory(recipeRepository);
        editRecipeViewModel = new ViewModelProvider(this, factory).get(EditRecipeViewModel.class);

        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        loadRecipeDetails();

        buttonAddIngredient.setOnClickListener(v -> addIngredientInput());
        buttonSaveRecipe.setOnClickListener(v -> saveRecipe());
        imageViewRecipe.setOnClickListener(this::onSelectImageClick);
    }

    private void loadRecipeDetails() {
        editRecipeViewModel.getRecipeById(recipeId).observe(this, recipe -> {
            if (recipe != null) {
                this.recipe = recipe;
                editTextTitle.setText(recipe.getTitle());
                editTextDescription.setText(recipe.getDescription());
                editTextCookingTime.setText(String.valueOf(recipe.getCookingTime()));
                spinnerCategory.setSelection(getCategoryPosition(recipe.getCategory()));

                Bitmap bitmap = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length);
                imageViewRecipe.setImageBitmap(bitmap);

                loadIngredients(recipeId);
            }
        });
    }

    private void loadIngredients(int recipeId) {
        editRecipeViewModel.getIngredientsForRecipe(recipeId).observe(this, ingredientWithQuantities -> {
            ingredientsContainer.removeAllViews();
            ingredientsContainer.addView(initialIngredientRow);
            ingredientInputs.clear();
            quantityInputs.clear();
            for (IngredientWithQuantity ingredient : ingredientWithQuantities) {
                addIngredientInput(ingredient.getName(), ingredient.getQuantity());
            }
        });
    }

    private void addIngredientInput() {
        addIngredientInput("", "");
    }

    private void addIngredientInput(String ingredientName, String ingredientQuantity) {
        LinearLayout newIngredientRow = new LinearLayout(this);
        newIngredientRow.setOrientation(LinearLayout.HORIZONTAL);

        EditText newIngredientInput = new EditText(this);
        newIngredientInput.setHint("Ingredient");
        newIngredientInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
        newIngredientInput.setText(ingredientName);
        newIngredientRow.addView(newIngredientInput);

        EditText newQuantityInput = new EditText(this);
        newQuantityInput.setHint("Qty (g)");
        newQuantityInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newQuantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newQuantityInput.setText(ingredientQuantity);
        newIngredientRow.addView(newQuantityInput);

        ingredientsContainer.addView(newIngredientRow, ingredientsContainer.getChildCount() - 1);
        ingredientInputs.add(newIngredientInput);
        quantityInputs.add(newQuantityInput);
    }

    private void saveRecipe() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int cookingTime = Integer.parseInt(editTextCookingTime.getText().toString());
        String category = spinnerCategory.getSelectedItem().toString();

        Bitmap bitmap = ((BitmapDrawable) imageViewRecipe.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        List<Ingredient> ingredients = new ArrayList<>();
        List<String> quantities = new ArrayList<>();

        for (int i = 0; i < ingredientInputs.size(); i++) {
            String ingredientName = ingredientInputs.get(i).getText().toString().trim();
            String quantity = quantityInputs.get(i).getText().toString().trim();

            if (!ingredientName.isEmpty() && !quantity.isEmpty()) {
                ingredients.add(new Ingredient(ingredientName));
                quantities.add(quantity);
            }
        }

        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setCookingTime(cookingTime);
        recipe.setCategory(category);
        recipe.setImage(imageBytes);

        editRecipeViewModel.updateRecipeWithIngredients(recipe, ingredients.toArray(new Ingredient[0]), quantities.toArray(new String[0]));

        Toast.makeText(this, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }


    private int getCategoryPosition(String category) {
        String[] categories = getResources().getStringArray(R.array.category_array);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                return i;
            }
        }
        return 0;
    }
    public void onSelectImageClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewRecipe.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
