package com.example.gestionrecettes.presentation.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;
import com.example.gestionrecettes.data.databaseClasses.UserDataSource;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepositoryImpl;
import com.example.gestionrecettes.presentation.adapters.RecipeAdapter;
import com.example.gestionrecettes.presentation.viewmodels.ProfileViewModel;
import com.example.gestionrecettes.presentation.viewmodels.ProfileViewModelFactory;
import com.example.gestionrecettes.presentation.viewmodels.RecipeViewModel;
import com.example.gestionrecettes.presentation.viewmodels.RecipeViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

public class home_activity extends AppCompatActivity {
    private RecipeViewModel viewModel;
    private ProfileViewModel profileViewModel;
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private ImageButton profileButton;
    private TextView userName;
    private ActivityResultLauncher<Intent> recipeDetailsLauncher;
    private ActivityResultLauncher<Intent> startForResult;
    private String currentCategory = "All"; // Default category
    private TextInputEditText searchInput; // Assuming you have this reference


    private int getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return sharedPref.getInt("USER_ID", -1);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//       Add "All" button first
        LinearLayout buttonLayout = findViewById(R.id.categoryButtonLayout);
        addButton("All", buttonLayout);
        String[] categories = getResources().getStringArray(R.array.category_array);
        for (String category : categories) {
            Button button = new Button(new ContextThemeWrapper(this, R.style.CategoryButtonStyle), null, 0);

            button.setText(category);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 0, 10, 0); // Margins between buttons
            button.setLayoutParams(params);

            button.setOnClickListener(v -> {
                updateCategoryFilter(category);
                updateButtonSelection(buttonLayout, category);
            });

            buttonLayout.addView(button);
        }

        userName = findViewById(R.id.home_username);
        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int userId = getUserIdFromPreferences(); // Retrieve user ID from SharedPreferences
        if (userId == -1) {
            // Handle the case where no user ID is found
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            finish();  // Optionally close the activity
            return;
        }
        RecipeDataSource dataSource = new RecipeDataSource(this);
        RecipeRepository repository = new RecipeRepositoryImpl(dataSource);
        RecipeViewModelFactory factory = new RecipeViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(RecipeViewModel.class);
        // Observe the LiveData from the ViewModel
        viewModel.getRecipesWithUserDetails().observe(this, recipesWithUser -> {
            if (adapter == null) {
                adapter = new RecipeAdapter(this, recipesWithUser,userId);
                recipesRecyclerView.setAdapter(adapter);
            } else {
                adapter.updateRecipes(recipesWithUser); // Make sure this method updates both lists
            }
        });
        profileButton = findViewById(R.id.imageButton);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(home_activity.this, profile_activity.class);
            startActivity(intent);
        });


        UserDataSource userDataSource = new UserDataSource(this);
        UserRepository userRepository = new UserRepositoryImpl(userDataSource);
        ProfileViewModelFactory userFactory = new ProfileViewModelFactory(repository, userRepository, userId);
        profileViewModel = new ViewModelProvider(this, userFactory).get(ProfileViewModel.class);
        profileViewModel.getUserData().observe(this, user -> {
            if (user != null) {
                userName.setText("Hello " + user.getName());
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        });
        TextInputEditText searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });
    }
    private void updateButtonSelection(LinearLayout layout, String selectedCategory) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            Button button = (Button) layout.getChildAt(i);
            boolean isSelected = button.getText().toString().equals(selectedCategory);
            button.setSelected(isSelected);
        }
    }
    private void updateCategoryFilter(String category) {
        currentCategory = category; // Update the current category
        if (adapter != null) {
            if (searchInput != null) {
                // Apply the filter with the current text in the search input
                adapter.setCurrentCategory(category);
                adapter.getFilter().filter(searchInput.getText().toString());
            } else {
                // If searchInput is not initialized yet, use an empty string
                adapter.setCurrentCategory(category);
                adapter.getFilter().filter("");
            }
            updateButtonSelection((LinearLayout) findViewById(R.id.categoryButtonLayout), category);
        }
    }
    private void addButton(String category, LinearLayout layout) {
        Button button = new Button(new ContextThemeWrapper(this, R.style.CategoryButtonStyle), null, 0);
        button.setText(category);
        button.setOnClickListener(v -> {
            updateCategoryFilter(category);
            updateButtonSelection(layout, category);
        });
        layout.addView(button);
        if (category.equals("All")) {
            button.setSelected(true); // "All" is selected by default
        }
    }
}
