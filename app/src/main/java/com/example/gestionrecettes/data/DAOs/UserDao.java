package com.example.gestionrecettes.data.DAOs;

import static androidx.room.OnConflictStrategy.REPLACE;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gestionrecettes.domain.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void insert(User user);

    @Query("SELECT * FROM users WHERE userId = :userId")
    User getUserById(int userId);
    @Query("SELECT * FROM users")
    List<User> getAll();
}
