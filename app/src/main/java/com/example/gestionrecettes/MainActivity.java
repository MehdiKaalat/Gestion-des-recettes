package com.example.gestionrecettes;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.gestionrecettes.data.DAOs.UserDao;
import com.example.gestionrecettes.domain.entities.User;
import com.example.gestionrecettes.framework.database.AppDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        appDatabase = AppDatabase.getInstance(getApplicationContext());


        // Create a new user object
        User user = new User();
        user.setName("example_username");
        user.setPassword("example_password");
        user.setEmail("example@example.com");

        // Get the UserDao instance from the database
        UserDao userDao = appDatabase.userDao();

        // Insert the user into the database
        userDao.insert(user);
        List<User> userList = userDao.getAll();

        // Log the details of each user
        for (User user1 : userList) {
            Log.d(TAG, "User ID: " + user1.getUserId() +
                    ", Username: " + user1.getName() +
                    ", Email: " + user.getEmail());
        }
    }


}