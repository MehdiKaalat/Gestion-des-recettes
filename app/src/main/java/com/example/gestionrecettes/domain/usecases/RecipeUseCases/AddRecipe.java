package com.example.gestionrecettes.domain.usecases.RecipeUseCases;

import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepository;
import com.example.gestionrecettes.domain.entities.Recipe;
import com.example.gestionrecettes.data.repositories.RecipeRepo.RecipeRepositoryImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRecipe {
    private final RecipeRepository recipeRepository;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public AddRecipe(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public interface Callback {
        void onSuccess();
        void onError(Exception e);
    }

    public void execute(Recipe recipe, Callback callback) {
        executor.execute(() -> {
            try {
                recipeRepository.insertRecipe(recipe);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
}
