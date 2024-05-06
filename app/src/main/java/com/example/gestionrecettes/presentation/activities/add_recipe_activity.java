package com.example.gestionrecettes.presentation.activities;

import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.databaseClasses.RecipeDataSource;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;
import com.example.gestionrecettes.domain.entities.Recipe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class add_recipe_activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for picking an image
    private Spinner spinnerCategory;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextCookingTime;
    private ImageView imageViewRecipe;
    private Button buttonSaveRecipe;
    private RecipeRepository recipeRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        recipeRepository = new RecipeRepositoryImpl(new RecipeDataSource(this));
        // Initialize all views
        imageViewRecipe = findViewById(R.id.imageViewRecipe);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCookingTime = findViewById(R.id.editTextCookingTime);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);

        setupSpinner();
        buttonSaveRecipe.setOnClickListener(v -> saveRecipe());
    }

    // This method is called when the ImageView is clicked
//    public void onSelectImageClick(View view) {
//        openFileChooser();
//    }
//
//    private void openFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
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
    private void saveRecipe() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int cookingTime = Integer.parseInt(editTextCookingTime.getText().toString());
        String category = spinnerCategory.getSelectedItem().toString();

        Bitmap bitmap = ((BitmapDrawable) imageViewRecipe.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        Recipe recipe = new Recipe(1,title, description, imageBytes, cookingTime, category);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                recipeRepository.insertRecipe(recipe);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(add_recipe_activity.this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}
