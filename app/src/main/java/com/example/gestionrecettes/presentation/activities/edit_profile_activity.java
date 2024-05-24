package com.example.gestionrecettes.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.gestionrecettes.databinding.ActivityEditProfileBinding;
import com.example.gestionrecettes.domain.entities.User;
import com.example.gestionrecettes.presentation.viewmodels.ProfileViewModel;
import com.example.gestionrecettes.presentation.viewmodels.ProfileViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class edit_profile_activity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private ImageView imageViewPdp;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageViewPdp = findViewById(R.id.imageViewPdp);

        // Initialize the data sources and repositories
        UserDataSource userDataSource = new UserDataSource(getApplicationContext());
        UserRepository userRepository = new UserRepositoryImpl(userDataSource);
        RecipeDataSource recipeDataSource = new RecipeDataSource(getApplicationContext());
        RecipeRepository recipeRepository = new RecipeRepositoryImpl(recipeDataSource);

        // Get user ID from intent or SharedPreferences
        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Initialize the ViewModel with both repositories
        ProfileViewModelFactory factory = new ProfileViewModelFactory(recipeRepository, userRepository, userId);
        profileViewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);

        // Load user data
        profileViewModel.getUserData().observe(this, user -> {
            if (user != null) {
                binding.nameEdit.setText(user.getName());
                binding.emailEdit.setText(user.getEmail());
                binding.passwordEdit.setText(user.getPassword());
                binding.passwordEditConfirmation.setText(user.getPassword());
                if (user.getPdp() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(user.getPdp(), 0, user.getPdp().length);
                    imageViewPdp.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewPdp.setOnClickListener(v -> selectImage());

        binding.saveEdit.setOnClickListener(v -> updateUser(userId));
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }
    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Resize and compress the image
                selectedImageBitmap = getResizedBitmap(selectedImageBitmap, 200, 200); // Resize to 200x200 or another suitable size
                imageViewPdp.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUser(int userId) {
        String name = binding.nameEdit.getText().toString().trim();
        String email = binding.emailEdit.getText().toString().trim();
        String password = binding.passwordEdit.getText().toString().trim();
        String confirmPassword = binding.passwordEditConfirmation.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the selected image to a byte array
        byte[] imageBytes = null;
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageBytes = baos.toByteArray();
            Log.d("EditProfileActivity", "imageBytes length: " + imageBytes.length);
        }

        // Create a User object
        User user = new User(userId, name, email, password, imageBytes);

        // Update the user
        profileViewModel.updateUser(user);
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        // Set result to OK
        // Create an Intent and put the updated data into it
        Intent returnIntent = new Intent();
        returnIntent.putExtra("name", name);
        returnIntent.putExtra("image", imageBytes);

        // Set the result of the activity
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
