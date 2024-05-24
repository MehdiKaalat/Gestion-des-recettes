package com.example.gestionrecettes.presentation.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.domain.entities.User;

public class SignupViewModel extends ViewModel {
    private final UserRepository userRepository;

    public SignupViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public void register(byte[] imagePdp, String name, String email, String password) {
//        userRepository.register(imagePdp, name, email, password);
//    }
    public LiveData<Integer> register(byte[] imagePdp, String name, String email, String password) {
        MutableLiveData<Integer> userIdLiveData = new MutableLiveData<>();
        new Thread(() -> {
            userRepository.register(imagePdp, name, email, password);
        }).start();
        return userIdLiveData;
    }

}
