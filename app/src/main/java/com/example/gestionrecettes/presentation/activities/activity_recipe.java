package com.example.gestionrecettes.presentation.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.DTOs.IngredientWithQuantity;
import com.example.gestionrecettes.presentation.adapters.IngredientAdapter;
import com.example.gestionrecettes.presentation.viewmodels.RecipeViewModel;
import com.example.gestionrecettes.presentation.viewmodels.RecipeViewModelFactory;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;
import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;

import java.util.List;

public class activity_recipe extends AppCompatActivity {
    private static final int EDIT_RECIPE_REQUEST_CODE = 1;
    private int recipeOwnerId;
    private int signedInUserId;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);


        // Retrieve data from the Intent
        String title = getIntent().getStringExtra("RECIPE_TITLE");
        byte[] imageBytes = getIntent().getByteArrayExtra("RECIPE_IMAGE");
        String description = getIntent().getStringExtra("RECIPE_DESCRIPTION");
        String category = getIntent().getStringExtra("RECIPE_CATEGORY");
        int cookingTime = getIntent().getIntExtra("COOKING_TIME", 0);
        String userName = getIntent().getStringExtra("USER_NAME");

        recipeOwnerId = getIntent().getIntExtra("RECIPE_OWNER_ID", -1); // Get the recipe owner ID
        signedInUserId = getIntent().getIntExtra("SIGNED_IN_USER_ID", -1); // Get the signed-in user ID

        // Set data to views
        ((TextView) findViewById(R.id.title_recipe)).setText(title);
        ((TextView) findViewById(R.id.textView12)).setText("By " + userName);
        ((TextView) findViewById(R.id.category)).setText(category + " | " );
        ((TextView) findViewById(R.id.cookingTime)).setText(cookingTime + " mins");
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        ((ImageView) findViewById(R.id.img_item)).setImageBitmap(imageBitmap);

        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        // Initialize the ViewModel
        RecipeRepositoryImpl recipeRepository = new RecipeRepositoryImpl(new RecipeDataSource(this));
        RecipeViewModelFactory factory = new RecipeViewModelFactory(recipeRepository);
        recipeViewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);

        // Set up TabHost
        TabHost tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Ingredients");
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Procedure");
        tabHost.addTab(spec2);

        // Load data into the tabs
        loadIngredients();
        loadProcedure(description);
    }

    private void loadIngredients() {
        RecyclerView ingredientsList = findViewById(R.id.ingredients_list);
        ingredientsList.setLayoutManager(new LinearLayoutManager(this));

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        recipeViewModel.getIngredientsForRecipe(recipeId).observe(this, new Observer<List<IngredientWithQuantity>>() {
            @Override
            public void onChanged(List<IngredientWithQuantity> ingredientWithQuantities) {
                IngredientAdapter adapter = new IngredientAdapter(ingredientWithQuantities);
                ingredientsList.setAdapter(adapter);
            }
        });
    }

    private void loadProcedure(String description) {
        TextView procedureSteps = findViewById(R.id.procedure_steps);
        procedureSteps.setText(description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.edit_recipe);
        MenuItem deleteItem = menu.findItem(R.id.delete_recipe);

        // Check if the signed-in user is the owner of the recipe
        if (recipeOwnerId != signedInUserId) {
            editItem.setVisible(false);
            deleteItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_recipe) {
            Intent intent = new Intent(this, edit_recipe_activity.class);
            intent.putExtra("RECIPE_ID", getIntent().getIntExtra("RECIPE_ID", -1));
            startActivityForResult(intent, EDIT_RECIPE_REQUEST_CODE);
            return true;
        } else if (item.getItemId() == R.id.delete_recipe) {
            deleteRecipe();
            return true;
        } else if (item.getItemId() == R.id.share_recipe) {
            shareRecipe();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteRecipe() {
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        recipeViewModel.deleteRecipe(recipeId);
        Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void shareRecipe() {
        String title = getIntent().getStringExtra("RECIPE_TITLE");
        String description = getIntent().getStringExtra("RECIPE_DESCRIPTION");

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n" + description);
        shareIntent.setType("text/plain");

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_RECIPE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh the recipe details
            loadRecipeDetails();
        }
    }
    private void loadRecipeDetails() {
        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        recipeViewModel.getRecipeById(recipeId).observe(this, recipe -> {
            if (recipe != null) {
                ((TextView) findViewById(R.id.title_recipe)).setText(recipe.getTitle());
                ((TextView) findViewById(R.id.textView12)).setText("By " + getIntent().getStringExtra("USER_NAME"));
                ((TextView) findViewById(R.id.category)).setText(recipe.getCategory());
                ((TextView) findViewById(R.id.cookingTime)).setText(" | " + recipe.getCookingTime() + " mins");
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(recipe.getImage(), 0, recipe.getImage().length);
                ((ImageView) findViewById(R.id.img_item)).setImageBitmap(imageBitmap);

                // Load ingredients and procedure again
                loadIngredients();
                loadProcedure(recipe.getDescription());
            }
        });
    }

}

