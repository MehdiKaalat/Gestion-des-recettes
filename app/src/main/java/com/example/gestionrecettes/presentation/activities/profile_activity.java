package com.example.gestionrecettes.presentation.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.DTOs.RecipeWithUserNameDTO;
import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;
import com.example.gestionrecettes.data.databaseClasses.UserDataSource;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepositoryImpl;
import com.example.gestionrecettes.presentation.adapters.RecipeAdapter;
import com.example.gestionrecettes.presentation.viewmodels.ProfileViewModelFactory;
import com.example.gestionrecettes.presentation.viewmodels.ProfileViewModel;

import java.util.List;

public class profile_activity extends AppCompatActivity {
    private ProfileViewModel viewModel;
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private TextView userName;
    private ImageView imagePdp;
    private ActivityResultLauncher<Intent> startForResult;
    private static final int PROFILE_ACTIVITY_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> editProfileLauncher;


    private int getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return sharedPref.getInt("USER_ID", -1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        userName = findViewById(R.id.user_name);
        imagePdp = findViewById(R.id.imagePdp);
        // Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        recipesRecyclerView = findViewById(R.id.recipes_profile);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int userId = getUserIdFromPreferences(); // Retrieve user ID from SharedPreferences
        if (userId == -1) {
            // Handle the case where no user ID is found
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish();  // Optionally close the activity
            return;
        }
        RecipeDataSource dataSource = new RecipeDataSource(this);
        UserDataSource userDataSource = new UserDataSource(this);
        RecipeRepository repository = new RecipeRepositoryImpl(dataSource);
        UserRepository userRepository = new UserRepositoryImpl(userDataSource);
        ProfileViewModelFactory factory = new ProfileViewModelFactory(repository, userRepository, userId);
        viewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);
        viewModel.getUserRecipes().observe(this, recipes -> {
            adapter = new RecipeAdapter(this, recipes, userId);
            recipesRecyclerView.setAdapter(adapter);
        });

        viewModel.getRecipeCount().observe(this, count -> {
            TextView recipeCountView = findViewById(R.id.nbr_recipes);
            recipeCountView.setText(String.valueOf(count));
        });

        viewModel.getUserData().observe(this, user -> {
            userName.setText(user.getName());
            // Display the profile picture
            if (user.getPdp() != null) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(user.getPdp(), 0, user.getPdp().length);
                imagePdp.setImageBitmap(imageBitmap);
            }
        });
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Extract the updated data from the returned Intent
                        Intent data = result.getData();
                        String updatedName = data.getStringExtra("name");
                        byte[] updatedPdp = data.getByteArrayExtra("image");  // Assuming a byte array for the profile image

                        // Update UI elements directly
                        userName.setText(updatedName);
                        if (updatedPdp != null) {
                            Bitmap imageBitmap = BitmapFactory.decodeByteArray(updatedPdp, 0, updatedPdp.length);
                            imagePdp.setImageBitmap(imageBitmap);
                        }

                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_recipe) {
            Intent intent = new Intent(this, add_recipe_activity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.edit_profile) {
            Intent intent = new Intent(this, edit_profile_activity.class);
            intent.putExtra("USER_ID", getUserIdFromPreferences()); // Pass any required data
            editProfileLauncher.launch(intent); // Correctly launch EditProfileActivity
            return true;
        } else if (id == R.id.logout) {
            handleLogout();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        fetchRecipesFromDatabase();
    }
    private void fetchRecipesFromDatabase() {
        int userId = getUserIdFromPreferences();
        viewModel.getUserRecipes().observe(this, recipes -> {
            if (adapter == null) {
                adapter = new RecipeAdapter(this, recipes, userId);
                recipesRecyclerView.setAdapter(adapter);
            } else {
                adapter.updateRecipes(recipes); // Make sure your adapter has this method
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh the RecyclerView
            fetchRecipesFromDatabase();
        }
    }
    private void handleLogout() {
        // Clear the shared preferences or any other storage where you hold login status
        SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
        editor.remove("USER_ID");
        editor.apply();
        // Intent to navigate back to the Login screen or any other appropriate screen
        Intent intent = new Intent(this, login_activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
