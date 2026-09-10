package com.aacode.bietdiet.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aacode.bietdiet.models.Favourite;
import com.aacode.bietdiet.repository.FavouriteRepository;

@RestController
@RequestMapping("/api/favourites")
@CrossOrigin(origins = "*")
public class FavouriteController {

    private final FavouriteRepository favouriteRepository;

    public FavouriteController(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    // ==========================================
    // ADD FAVOURITE
    // ==========================================

    @PostMapping
    public ResponseEntity<?> addFavourite(
            @RequestBody Favourite favourite) {

        // Check email
        if (favourite.getEmail() == null ||
                favourite.getEmail().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Email is required");
        }

        // Check recipe ID
        if (favourite.getRecipeId() == null ||
                favourite.getRecipeId().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Recipe ID is required");
        }

        // Check recipe name
        if (favourite.getRecipeName() == null ||
                favourite.getRecipeName().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Recipe name is required");
        }

        // Clean values
        favourite.setEmail(
                favourite.getEmail().trim()
        );

        favourite.setRecipeId(
                favourite.getRecipeId().trim()
        );

        favourite.setRecipeName(
                favourite.getRecipeName().trim()
        );

        // ==========================================
        // CHECK ALREADY EXISTS
        // ==========================================

        boolean alreadyExists =
                favouriteRepository.existsByEmailAndRecipeId(
                        favourite.getEmail(),
                        favourite.getRecipeId()
                );

        if (alreadyExists) {

            return ResponseEntity.ok(
                    "Recipe already added to favourites"
            );
        }

        // ==========================================
        // ADD DATE/TIME
        // ==========================================

        favourite.setAddedAt(
                LocalDateTime.now().toString()
        );

        // ==========================================
        // SAVE TO DATABASE
        // ==========================================

        Favourite saved =
                favouriteRepository.save(favourite);

        System.out.println(
                "Favourite saved -> Email: "
                        + saved.getEmail()
                        + ", Recipe ID: "
                        + saved.getRecipeId()
                        + ", Recipe Name: "
                        + saved.getRecipeName()
        );

        return ResponseEntity.ok(saved);
    }

    // ==========================================
    // GET USER FAVOURITES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Favourite>> getFavourites(
            @RequestParam String email) {

        List<Favourite> favourites =
                favouriteRepository
                        .findByEmailOrderByIdDesc(email);

        return ResponseEntity.ok(favourites);
    }

    // ==========================================
    // CHECK IF RECIPE IS FAVOURITE
    // ==========================================

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavourite(
            @RequestParam String email,
            @RequestParam String recipeId) {

        boolean exists =
                favouriteRepository
                        .existsByEmailAndRecipeId(
                                email,
                                recipeId
                        );

        return ResponseEntity.ok(exists);
    }

    // ==========================================
    // REMOVE ONE FAVOURITE
    // ==========================================

    @DeleteMapping
    public ResponseEntity<?> removeFavourite(
            @RequestParam String email,
            @RequestParam String recipeId) {

        favouriteRepository.deleteByEmailAndRecipeId(
                email,
                recipeId
        );

        return ResponseEntity.ok(
                "Favourite removed successfully"
        );
    }

    // ==========================================
    // CLEAR ALL USER FAVOURITES
    // ==========================================

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearFavourites(
            @RequestParam String email) {

        favouriteRepository.deleteByEmail(email);

        return ResponseEntity.ok(
                "All favourites cleared successfully"
        );
    }
}