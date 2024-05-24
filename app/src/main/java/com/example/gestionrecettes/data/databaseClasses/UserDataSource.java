package com.example.gestionrecettes.data.databaseClasses;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.gestionrecettes.data.DAOs.UserDao;
import com.example.gestionrecettes.domain.entities.User;
import com.example.gestionrecettes.framework.database.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserDataSource {
    private final UserDao userDao;
    public UserDataSource(Context context) {
        // Initialize the Room database and obtain the DAO
        AppDatabase appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "myDB")
                .allowMainThreadQueries()
                .build();
        userDao = appDatabase.userDao();
    }

    public UserDao getUserDao() {
        return userDao;
    }
    public LiveData<User> login(String email, String password) {
        return userDao.getUserByEmailAndPassword(email, password);
    }
    public void register(byte[] imagePdp, String name, String email, String password) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            user.setPdp(imagePdp);
            userDao.insert(user);
        });
    }
    public void updateUser(User user) {
        userDao.update(user);
    }


}
