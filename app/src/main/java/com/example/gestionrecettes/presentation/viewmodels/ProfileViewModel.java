package com.example.gestionrecettes.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionrecettes.data.DTOs.RecipeWithUserNameDTO;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.data.repositories.UserRepo.UserRepository;
import com.example.gestionrecettes.domain.entities.User;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private LiveData<List<RecipeWithUserNameDTO>> userRecipes;
    private RecipeRepository recipeRepository;
    private UserRepository userRepository;
    private LiveData<Integer> recipeCount;
    private LiveData<User> userData;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final int userId;

    public ProfileViewModel(RecipeRepository recipeRepository,UserRepository userRepository, int userId) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.userId = userId;
        userRecipes = recipeRepository.getRecipesForUser(userId);
        recipeCount = recipeRepository.getRecipeCountByUserId(userId);
        userData = userRepository.getUserById(userId);
        loadUserData(userId);
    }

    public LiveData<List<RecipeWithUserNameDTO>> getUserRecipes() {
        return userRecipes;
    }
    public LiveData<Integer> getRecipeCount() {
        return recipeCount;
    }

    public LiveData<User> getUserData() {
        return userData;
    }
    public void loadUserData(int userId) {
        User user = userRepository.getUserById(userId).getValue();
        userLiveData.setValue(user);
    }
    public void updateUser(User user) {
        userRepository.updateUser(user);
        userLiveData.setValue(user);
    }
    public void refreshUserRecipes() {
        userRecipes = recipeRepository.getRecipesForUser(userId);
    }
}
