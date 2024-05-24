package com.example.gestionrecettes.data.repositories.UserRepo;

import androidx.lifecycle.LiveData;

import com.example.gestionrecettes.data.DAOs.UserDao;
import com.example.gestionrecettes.data.databaseClasses.UserDataSource;
import com.example.gestionrecettes.domain.entities.User;

public class UserRepositoryImpl implements UserRepository{
    private UserDao userDao;
    private UserDataSource userDataSource;
    public UserRepositoryImpl(UserDataSource userDataSource) {
        this.userDao = userDataSource.getUserDao();
        this.userDataSource = userDataSource;
    }
    @Override
    public LiveData<User> getUserById(int userId) {
        return userDao.getLiveUserById(userId);
    }
    @Override
    public LiveData<User> login(String email, String password) {
        return userDataSource.login(email, password);
    }

    @Override
    public void register(byte[] imagePdp, String name, String email, String password) {
        userDataSource.register(imagePdp,name, email, password);
    }

    @Override
    public void updateUser(User user) {
        userDataSource.updateUser(user);
    }

}
