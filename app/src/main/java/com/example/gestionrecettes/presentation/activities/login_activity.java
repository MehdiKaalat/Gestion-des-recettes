package com.example.gestionrecettes.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
import com.example.gestionrecettes.databinding.ActivityLoginBinding;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.presentation.viewmodels.AddRecipeViewModel;
import com.example.gestionrecettes.presentation.viewmodels.AddRecipeViewModelFactory;
import com.example.gestionrecettes.presentation.viewmodels.LoginViewModel;
import com.example.gestionrecettes.presentation.viewmodels.LoginViewModelFactory;
import com.example.gestionrecettes.presentation.viewmodels.SignupViewModel;
import com.example.gestionrecettes.presentation.viewmodels.SignupViewModelFactory;

import java.io.ByteArrayOutputStream;

public class login_activity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private Bitmap selectedImageBitmap;
    private SignupViewModel signupViewModel;
    private AddRecipeViewModel addRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout with view binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize UserDataSource and UserRepository
        UserDataSource userDataSource = new UserDataSource(getApplicationContext());
        UserRepository userRepository = new UserRepositoryImpl(userDataSource);

        // Initialize SignupViewModel
        SignupViewModelFactory signupFactory = new SignupViewModelFactory(userRepository);
        signupViewModel = new ViewModelProvider(this, signupFactory).get(SignupViewModel.class);

        // Initialize AddRecipeViewModel
        RecipeDataSource recipeDataSource = new RecipeDataSource(getApplicationContext());
        RecipeRepository recipeRepository = new RecipeRepositoryImpl(recipeDataSource);
        AddRecipeViewModelFactory addRecipeFactory = new AddRecipeViewModelFactory(recipeRepository, userRepository);
        addRecipeViewModel = new ViewModelProvider(this, addRecipeFactory).get(AddRecipeViewModel.class);

        LoginViewModelFactory factory = new LoginViewModelFactory(userRepository);
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        binding.buttonLogin.setOnClickListener(v -> login());

        binding.goSignupPage.setOnClickListener(v -> navigateToSignUp());
//        autoCreateAccountAndRecipe();
    }

    private void login() {
        String email = binding.emailSignin.getText().toString().trim();
        String password = binding.passwordSignin.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        loginViewModel.login(email, password).observe(this, user -> {
            if (user != null) {
                saveUserId(user.getUserId());
                Toast.makeText(login_activity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                navigateToHomeScreen();
            } else {
                Toast.makeText(login_activity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, home_activity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(this, signup_activity.class);
        startActivity(intent);
    }

    private void saveUserId(int userId) {
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("USER_ID", userId);
        editor.apply();
    }

//    private void autoCreateAccountAndRecipe() {
//        String name = "El Mehdi Kaalat";
//        String email = "mehdi@gmail.com";
//        String password = "mehdi";
//        selectedImageBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pdp);
//
//        byte[] imageBytes = null;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        imageBytes = baos.toByteArray();
//        Log.d(TAG, "imageBytes length: " + imageBytes.length);
//
//        signupViewModel.register(imageBytes, name, email, password).observe(this, this::createRecipeForUser);
//    }
//    private void createRecipeForUser(int userId) {
//        String title = "Traditional spare ribs baked";
//        String description = "This is a sample recipe created automatically.";
//        String category = "Dessert";
//        int cookingTime = 30;
//
//        Bitmap recipeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img1);
//        byte[] image = null;
//
//        if (recipeBitmap != null) {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            recipeBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//            image = baos.toByteArray();
//        }
//
//        Recipe recipe = new Recipe(userId, title, description, image, cookingTime, category);
//
//        Ingredient[] ingredients = new Ingredient[] {
//                new Ingredient("Flour"),
//                new Ingredient("Sugar"),
//                new Ingredient("Eggs")
//        };
//
//        String[] quantities = new String[] { "200g", "100g", "3" };
//
//        Log.d(TAG, "Inserting recipe with userId: " + userId);
//        new Thread(() -> {
//            addRecipeViewModel.insertRecipeWithIngredients(recipe, ingredients, quantities);
//            runOnUiThread(() -> {
//                Toast.makeText(this, "Recipe created successfully", Toast.LENGTH_SHORT).show();
//                finish();
//            });
//        }).start();
//    }
}
