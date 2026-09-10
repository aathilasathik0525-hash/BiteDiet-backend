package com.aacode.bietdiet.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.aacode.bietdiet.models.Recipe;
import com.aacode.bietdiet.repository.RecipeRepository;
import com.aacode.bietdiet.service.RecipeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RecipeDataSeeder implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;
    private final ObjectMapper objectMapper;

    public RecipeDataSeeder(RecipeRepository recipeRepository, RecipeService recipeService) {
        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run(String... args) {
        try {
            long existingCount = recipeRepository.count();
            System.out.println("Current recipes count in database: " + existingCount);

            if (existingCount < 280) {
                System.out.println("Starting automatic recipe seeding from recipes_seed.json...");
                InputStream inputStream = getClass().getResourceAsStream("/recipes_seed.json");
                if (inputStream == null) {
                    System.err.println("Could not find /recipes_seed.json in classpath!");
                    return;
                }

                List<Recipe> recipes = objectMapper.readValue(inputStream, new TypeReference<List<Recipe>>() {});
                System.out.println("Parsed " + recipes.size() + " recipes from seed file. Migrating to MySQL...");

                List<Recipe> saved = recipeService.bulkMigrateRecipes(recipes);
                System.out.println("Successfully seeded " + saved.size() + " new recipes into MySQL database!");
                System.out.println("Total recipes in database now: " + recipeRepository.count());
            } else {
                System.out.println("Recipe database is already populated with " + existingCount + " records.");
            }
        } catch (Exception e) {
            System.err.println("Error during recipe data seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
