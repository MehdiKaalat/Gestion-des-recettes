package com.example.gestionrecettes.presentation.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionrecettes.R;
import com.example.gestionrecettes.data.databaseClasses.UserDataSource;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepositoryImpl;
import com.example.gestionrecettes.databinding.ActivitySignupBinding;
import com.example.gestionrecettes.domain.entities.Ingredient;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.presentation.viewmodels.AddRecipeViewModel;
import com.example.gestionrecettes.presentation.viewmodels.LoginViewModel;
import com.example.gestionrecettes.presentation.viewmodels.LoginViewModelFactory;
import com.example.gestionrecettes.presentation.viewmodels.RecipeViewModel;
import com.example.gestionrecettes.presentation.viewmodels.SignupViewModel;
import com.example.gestionrecettes.presentation.viewmodels.SignupViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class signup_activity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private SignupViewModel signupViewModel;
    private ImageView imageViewPdp;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedImageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageViewPdp = findViewById(R.id.imageViewPdp);

        UserDataSource userDataSource = new UserDataSource(getApplicationContext());
        UserRepository userRepository = new UserRepositoryImpl(userDataSource);
        SignupViewModelFactory factory = new SignupViewModelFactory(userRepository);
        signupViewModel = new ViewModelProvider(this, factory).get(SignupViewModel.class);

//        // Initialize the ViewModel
//        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        binding.signupButton.setOnClickListener(v -> registerUser());
        imageViewPdp.setOnClickListener(v -> selectImage());
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
    private void registerUser() {
        String name = binding.nameSignup.getText().toString().trim();
        String email = binding.emailSignup.getText().toString().trim();
        String password = binding.passwordSignup.getText().toString().trim();
        String confirmPassword = binding.passwordSignupConfirmation.getText().toString().trim();

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
            Log.d("SignupActivity", "imageBytes length: " + imageBytes.length);
        }
        // Now register the user
        signupViewModel.register(imageBytes ,name, email, password);
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        finish();  // Optionally close the activity
    }

}
