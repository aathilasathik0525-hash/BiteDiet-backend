package com.aacode.bietdiet.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aacode.bietdiet.models.Recipe;
import com.aacode.bietdiet.repository.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public List<Recipe> getRecipesByPatientType(String patientType) {
        if (patientType == null || patientType.trim().isEmpty()) {
            return getAllRecipes();
        }
        return recipeRepository.findByPatientTypeIgnoreCase(patientType.trim());
    }

    public List<Recipe> getRecipesByCategory(String category) {
        if (category == null || category.trim().isEmpty() || "all".equalsIgnoreCase(category.trim())) {
            return getAllRecipes();
        }
        return recipeRepository.findByCategoryIgnoreCase(category.trim());
    }

    public List<Recipe> getRecipesByPatientTypeAndCategory(String patientType, String category) {
        if (category == null || category.trim().isEmpty() || "all".equalsIgnoreCase(category.trim())) {
            return getRecipesByPatientType(patientType);
        }
        if (patientType == null || patientType.trim().isEmpty()) {
            return getRecipesByCategory(category);
        }
        return recipeRepository.findByPatientTypeIgnoreCaseAndCategoryIgnoreCase(patientType.trim(), category.trim());
    }

    public List<Recipe> searchRecipes(String patientType, String category, String query) {
        String cleanType = (patientType != null && !patientType.trim().isEmpty()) ? patientType.trim() : null;
        String cleanCat = (category != null && !category.trim().isEmpty() && !"all".equalsIgnoreCase(category.trim())) ? category.trim() : null;
        String cleanQuery = (query != null && !query.trim().isEmpty()) ? query.trim() : null;

        return recipeRepository.searchRecipes(cleanType, cleanCat, cleanQuery);
    }

    public Recipe createRecipe(Recipe recipe) {
        if (recipe.getCreatedAt() == null || recipe.getCreatedAt().isEmpty()) {
            recipe.setCreatedAt(LocalDateTime.now().toString());
        }
        recipe.setUpdatedAt(LocalDateTime.now().toString());
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> updateRecipe(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id).map(existing -> {
            if (updatedRecipe.getRecipeName() != null) existing.setRecipeName(updatedRecipe.getRecipeName());
            if (updatedRecipe.getPatientType() != null) existing.setPatientType(updatedRecipe.getPatientType());
            if (updatedRecipe.getCategory() != null) existing.setCategory(updatedRecipe.getCategory());
            if (updatedRecipe.getDescription() != null) existing.setDescription(updatedRecipe.getDescription());
            if (updatedRecipe.getImageUrl() != null) existing.setImageUrl(updatedRecipe.getImageUrl());
            if (updatedRecipe.getIngredients() != null) existing.setIngredients(updatedRecipe.getIngredients());
            if (updatedRecipe.getInstructions() != null) existing.setInstructions(updatedRecipe.getInstructions());
            if (updatedRecipe.getPreparationTime() != null) existing.setPreparationTime(updatedRecipe.getPreparationTime());
            if (updatedRecipe.getCookingTime() != null) existing.setCookingTime(updatedRecipe.getCookingTime());
            if (updatedRecipe.getTotalTime() != null) existing.setTotalTime(updatedRecipe.getTotalTime());
            if (updatedRecipe.getServings() != null) existing.setServings(updatedRecipe.getServings());
            if (updatedRecipe.getCalories() != null) existing.setCalories(updatedRecipe.getCalories());
            if (updatedRecipe.getProtein() != null) existing.setProtein(updatedRecipe.getProtein());
            if (updatedRecipe.getCarbohydrates() != null) existing.setCarbohydrates(updatedRecipe.getCarbohydrates());
            if (updatedRecipe.getFat() != null) existing.setFat(updatedRecipe.getFat());
            if (updatedRecipe.getFiber() != null) existing.setFiber(updatedRecipe.getFiber());
            if (updatedRecipe.getSodium() != null) existing.setSodium(updatedRecipe.getSodium());
            if (updatedRecipe.getDifficultyLevel() != null) existing.setDifficultyLevel(updatedRecipe.getDifficultyLevel());

            existing.setUpdatedAt(LocalDateTime.now().toString());
            return recipeRepository.save(existing);
        });
    }

    public boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public List<Recipe> bulkMigrateRecipes(List<Recipe> recipes) {
        List<Recipe> savedList = new ArrayList<>();
        for (Recipe r : recipes) {
            String name = r.getRecipeName() != null ? r.getRecipeName().trim() : "";
            String pType = r.getPatientType() != null ? r.getPatientType().trim() : "Normal";

            if (name.isEmpty()) continue;

            boolean exists = recipeRepository.existsByRecipeNameIgnoreCaseAndPatientTypeIgnoreCase(name, pType);
            if (!exists) {
                if (r.getCreatedAt() == null || r.getCreatedAt().isEmpty()) {
                    r.setCreatedAt(LocalDateTime.now().toString());
                }
                r.setUpdatedAt(LocalDateTime.now().toString());
                savedList.add(recipeRepository.save(r));
            }
        }
        return savedList;
    }
}
