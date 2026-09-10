package com.aacode.bietdiet.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aacode.bietdiet.models.MealPlan;
import com.aacode.bietdiet.repository.MealPlanRepository;

@RestController
@RequestMapping("/api/meal-plan")
@CrossOrigin(origins = "*")
public class MealPlanController {

    private final MealPlanRepository mealPlanRepository;

    public MealPlanController(MealPlanRepository mealPlanRepository) {
        this.mealPlanRepository = mealPlanRepository;
    }

    // ==========================================
    // GET USER MEAL PLAN
    // ==========================================
    @GetMapping
    public ResponseEntity<?> getMealPlan(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        Optional<MealPlan> mealPlan = mealPlanRepository.findByEmail(email.trim());
        if (mealPlan.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "email", email.trim(),
                    "planData", "{}",
                    "message", "No meal plan found"
            ));
        }

        return ResponseEntity.ok(mealPlan.get());
    }

    // ==========================================
    // SAVE OR UPDATE MEAL PLAN
    // ==========================================
    @PostMapping
    public ResponseEntity<?> saveMealPlan(@RequestBody MealPlan planRequest) {
        if (planRequest.getEmail() == null || planRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        String email = planRequest.getEmail().trim();
        Optional<MealPlan> existingPlan = mealPlanRepository.findByEmail(email);

        MealPlan mealPlanToSave;
        if (existingPlan.isPresent()) {
            mealPlanToSave = existingPlan.get();
            if (planRequest.getPatientType() != null) {
                mealPlanToSave.setPatientType(planRequest.getPatientType());
            }
            if (planRequest.getPlanData() != null) {
                mealPlanToSave.setPlanData(planRequest.getPlanData());
            }
            mealPlanToSave.setUpdatedAt(LocalDateTime.now().toString());
        } else {
            mealPlanToSave = new MealPlan();
            mealPlanToSave.setEmail(email);
            mealPlanToSave.setPatientType(planRequest.getPatientType() != null ? planRequest.getPatientType() : "Normal");
            mealPlanToSave.setPlanData(planRequest.getPlanData() != null ? planRequest.getPlanData() : "{}");
            mealPlanToSave.setUpdatedAt(LocalDateTime.now().toString());
        }

        MealPlan saved = mealPlanRepository.save(mealPlanToSave);
        return ResponseEntity.ok(saved);
    }

    // ==========================================
    // DELETE / CLEAR MEAL PLAN
    // ==========================================
    @DeleteMapping
    public ResponseEntity<?> clearMealPlan(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        mealPlanRepository.deleteByEmail(email.trim());
        return ResponseEntity.ok(Map.of("message", "Meal plan cleared successfully"));
    }
}
