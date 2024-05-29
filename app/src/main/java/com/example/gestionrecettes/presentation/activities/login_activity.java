package com.example.gestionrecettes.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.List;

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
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserDataSource userDataSource = new UserDataSource(getApplicationContext());
        UserRepository userRepository = new UserRepositoryImpl(userDataSource);

        SignupViewModelFactory signupFactory = new SignupViewModelFactory(userRepository);
        signupViewModel = new ViewModelProvider(this, signupFactory).get(SignupViewModel.class);

        RecipeDataSource recipeDataSource = new RecipeDataSource(getApplicationContext());
        RecipeRepository recipeRepository = new RecipeRepositoryImpl(recipeDataSource);
        AddRecipeViewModelFactory addRecipeFactory = new AddRecipeViewModelFactory(recipeRepository, userRepository);
        addRecipeViewModel = new ViewModelProvider(this, addRecipeFactory).get(AddRecipeViewModel.class);

        LoginViewModelFactory factory = new LoginViewModelFactory(userRepository);
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
        createUser();
        binding.buttonLogin.setOnClickListener(v -> login());

        binding.goSignupPage.setOnClickListener(v -> navigateToSignUp());
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
    private void createUser(){
        selectedImageBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mehdi);
        String name = "El Mehdi Kaalat";
        String email = "mehdi@gmail.com";
        String password = "mehdi";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        signupViewModel.register(imageBytes ,name, email, password);
        saveRecipe();
        saveRecipe();
    }
    private void saveRecipe() {
        int userId = 1;
        String title = "Traditional Spare Ribs Baked";
        String description = "Preheat the oven to 150°C (300°F).\n" +
                "Season the spare ribs with salt, pepper, and your favorite dry rub.\n" +
                "Place the ribs on a baking sheet lined with foil.\n" +
                "Cover the ribs with another layer of foil and seal the edges.\n" +
                "Bake in the preheated oven for 2 hours.\n" +
                "Remove the top foil layer and brush the ribs with barbecue sauce.\n" +
                "Increase the oven temperature to 200°C (400°F) and bake for an additional 30 minutes.\n" +
                "Let the ribs rest for 10 minutes before serving.";
        int cookingTime = 150;
        String category = "Main Course";

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        List<Ingredient> ingredients = new ArrayList<>();
        List<String> quantities = new ArrayList<>();

        ingredients.add(new Ingredient("Spare Ribs"));
        quantities.add("1000");
        ingredients.add(new Ingredient("Salt"));
        quantities.add("10");
        ingredients.add(new Ingredient("Pepper"));
        quantities.add("5");
        ingredients.add(new Ingredient("Dry Rub"));
        quantities.add("30");
        ingredients.add(new Ingredient("Barbecue Sauce"));
        quantities.add("200");

        Recipe recipe = new Recipe(userId, title, description, imageBytes, cookingTime, category);
        addRecipeViewModel.insertRecipeWithIngredients(recipe, ingredients.toArray(new Ingredient[0]), quantities.toArray(new String[0]));
    }
}
