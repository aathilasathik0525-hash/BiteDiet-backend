package com.aacode.bietdiet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.aacode.bietdiet.models.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByPatientTypeIgnoreCase(String patientType);

    List<Recipe> findByPatientTypeIgnoreCaseAndCategoryIgnoreCase(String patientType, String category);

    List<Recipe> findByCategoryIgnoreCase(String category);

    List<Recipe> findByRecipeNameContainingIgnoreCase(String recipeName);

    List<Recipe> findByPatientTypeIgnoreCaseAndRecipeNameContainingIgnoreCase(String patientType, String recipeName);

    boolean existsByRecipeNameIgnoreCaseAndPatientTypeIgnoreCase(String recipeName, String patientType);

    @Query("SELECT r FROM Recipe r WHERE " +
           "(:patientType IS NULL OR LOWER(r.patientType) = LOWER(:patientType)) AND " +
           "(:category IS NULL OR LOWER(r.category) = LOWER(:category)) AND " +
           "(:query IS NULL OR LOWER(r.recipeName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Recipe> searchRecipes(
            @Param("patientType") String patientType,
            @Param("category") String category,
            @Param("query") String query
    );
}
