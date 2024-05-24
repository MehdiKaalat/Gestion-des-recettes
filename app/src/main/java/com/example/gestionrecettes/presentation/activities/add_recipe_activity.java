package com.example.gestionrecettes.presentation.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;
import com.example.gestionrecettes.data.databaseClasses.UserDataSource;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepositoryImpl;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.presentation.viewmodels.AddRecipeViewModel;
import com.example.gestionrecettes.presentation.viewmodels.AddRecipeViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class add_recipe_activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for picking an image
    private Spinner spinnerCategory;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextCookingTime;
    private ImageView imageViewRecipe;
    private Button buttonSaveRecipe;
    private Button buttonAddIngredient;
    private AddRecipeViewModel addRecipeViewModel;
    private List<EditText> ingredientInputs;
    private List<EditText> quantityInputs;

    private int getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return sharedPref.getInt("USER_ID", -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Initialize all views
        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCookingTime = findViewById(R.id.editTextCookingTime);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);
        buttonAddIngredient = findViewById(R.id.buttonAddIngredient);

        // Initialize lists for dynamic ingredient and quantity inputs
        ingredientInputs = new ArrayList<>();
        quantityInputs = new ArrayList<>();

        // Initialize the data sources and repositories
        RecipeDataSource recipeDataSource = new RecipeDataSource(getApplicationContext());
        UserDataSource userDataSource = new UserDataSource(getApplicationContext());
        RecipeRepository recipeRepository = new RecipeRepositoryImpl(recipeDataSource);
        UserRepository userRepository = new UserRepositoryImpl(userDataSource);

        // Initialize the ViewModel
        AddRecipeViewModelFactory factory = new AddRecipeViewModelFactory(recipeRepository, userRepository);
        addRecipeViewModel = new ViewModelProvider(this, factory).get(AddRecipeViewModel.class);

        setupSpinner();
        buttonAddIngredient.setOnClickListener(v -> addIngredientInput());
        buttonSaveRecipe.setOnClickListener(v -> saveRecipe());
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

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void addIngredientInput() {
        LinearLayout ingredientsContainer = findViewById(R.id.ingredientsContainer);

        LinearLayout newIngredientRow = new LinearLayout(this);
        newIngredientRow.setOrientation(LinearLayout.HORIZONTAL);

        EditText newIngredientInput = new EditText(this);
        newIngredientInput.setHint("Ingredient");
        newIngredientInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));
        newIngredientRow.addView(newIngredientInput);

        EditText newQuantityInput = new EditText(this);
        newQuantityInput.setHint("Quantity (g)");
        newQuantityInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        newQuantityInput.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newIngredientRow.addView(newQuantityInput);

        ingredientsContainer.addView(newIngredientRow);

        // Add to the list of ingredient and quantity inputs
        ingredientInputs.add(newIngredientInput);
        quantityInputs.add(newQuantityInput);
    }

    @SuppressLint("StaticFieldLeak")
    private void saveRecipe() {
        int userId = getUserIdFromPreferences(); // Retrieve user ID from SharedPreferences
        if (userId == -1) {
            // Handle the case where no user ID is found
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish();  // Optionally close the activity
            return;
        }
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int cookingTime = Integer.parseInt(editTextCookingTime.getText().toString());
        String category = spinnerCategory.getSelectedItem().toString();

        Bitmap bitmap = ((BitmapDrawable) imageViewRecipe.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        // Collect ingredients and quantities
        LinearLayout ingredientsContainer = findViewById(R.id.ingredientsContainer);
        int ingredientCount = ingredientsContainer.getChildCount();
        List<Ingredient> ingredients = new ArrayList<>();
        List<String> quantities = new ArrayList<>();

        for (int i = 0; i < ingredientCount; i++) {
            LinearLayout ingredientRow = (LinearLayout) ingredientsContainer.getChildAt(i);
            EditText ingredientInput = (EditText) ingredientRow.getChildAt(0);
            EditText quantityInput = (EditText) ingredientRow.getChildAt(1);

            String ingredientName = ingredientInput.getText().toString().trim();
            String quantity = quantityInput.getText().toString().trim();

            if (!ingredientName.isEmpty() && !quantity.isEmpty()) {
                ingredients.add(new Ingredient(ingredientName));
                quantities.add(quantity);
            }
        }

        // Create the recipe
        Recipe recipe = new Recipe(userId, title, description, imageBytes, cookingTime, category);

        // Save the recipe and ingredients
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                addRecipeViewModel.insertRecipeWithIngredients(recipe, ingredients.toArray(new Ingredient[0]), quantities.toArray(new String[0]));
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(add_recipe_activity.this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.execute();
    }
}
