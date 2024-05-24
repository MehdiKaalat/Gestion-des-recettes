package com.example.gestionrecettes.data.DAOs;

import static androidx.room.OnConflictStrategy.REPLACE;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestionrecettes.domain.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void insert(User user);

    @Query("SELECT * FROM users")
    List<User> getAll();
    @Query("SELECT * FROM users WHERE userId = :userId")
    User getUserById(int userId);
    @Query("SELECT * FROM users WHERE userId = :userId")
    LiveData<User> getLiveUserById(int userId);
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    LiveData<User> getUserByEmailAndPassword(String email, String password);
    @Update
    void update(User user);
}
