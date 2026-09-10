package com.aacode.bietdiet.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aacode.bietdiet.models.Recipe;
import com.aacode.bietdiet.service.RecipeService;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // ==========================================
    // GET ALL RECIPES
    // ==========================================
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(
            @RequestParam(required = false) String patientType,
            @RequestParam(required = false) String category) {

        if (patientType != null && category != null) {
            return ResponseEntity.ok(recipeService.getRecipesByPatientTypeAndCategory(patientType, category));
        } else if (patientType != null) {
            return ResponseEntity.ok(recipeService.getRecipesByPatientType(patientType));
        } else if (category != null) {
            return ResponseEntity.ok(recipeService.getRecipesByCategory(category));
        }

        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    // ==========================================
    // GET RECIPE BY ID
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Recipe not found with id: " + id));
        }
        return ResponseEntity.ok(recipe.get());
    }

    // ==========================================
    // GET RECIPES BY PATIENT TYPE
    // ==========================================
    @GetMapping("/patient-type/{patientType}")
    public ResponseEntity<List<Recipe>> getRecipesByPatientType(
            @PathVariable String patientType,
            @RequestParam(required = false) String category) {

        if (category != null && !category.trim().isEmpty()) {
            return ResponseEntity.ok(recipeService.getRecipesByPatientTypeAndCategory(patientType, category));
        }
        return ResponseEntity.ok(recipeService.getRecipesByPatientType(patientType));
    }

    // ==========================================
    // GET RECIPES BY CATEGORY
    // ==========================================
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Recipe>> getRecipesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(recipeService.getRecipesByCategory(category));
    }

    // ==========================================
    // SEARCH RECIPES
    // ==========================================
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String patientType,
            @RequestParam(required = false) String category) {

        String searchTerm = (query != null && !query.trim().isEmpty()) ? query : name;
        return ResponseEntity.ok(recipeService.searchRecipes(patientType, category, searchTerm));
    }

    // ==========================================
    // CREATE NEW RECIPE
    // ==========================================
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Recipe created = recipeService.createRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ==========================================
    // UPDATE RECIPE
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(
            @PathVariable Long id,
            @RequestBody Recipe recipe) {

        Optional<Recipe> updated = recipeService.updateRecipe(id, recipe);
        if (updated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Recipe not found with id: " + id));
        }
        return ResponseEntity.ok(updated.get());
    }

    // ==========================================
    // DELETE RECIPE
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        boolean deleted = recipeService.deleteRecipe(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Recipe not found with id: " + id));
        }
        return ResponseEntity.ok(Map.of("message", "Recipe deleted successfully", "id", id));
    }

    // ==========================================
    // BULK DATA MIGRATION
    // ==========================================
    @PostMapping("/bulk-migrate")
    public ResponseEntity<?> bulkMigrateRecipes(@RequestBody List<Recipe> recipes) {
        List<Recipe> saved = recipeService.bulkMigrateRecipes(recipes);
        return ResponseEntity.ok(Map.of(
                "message", "Migration complete",
                "totalReceived", recipes.size(),
                "totalInserted", saved.size(),
                "totalExisting", recipes.size() - saved.size()
        ));
    }
}
