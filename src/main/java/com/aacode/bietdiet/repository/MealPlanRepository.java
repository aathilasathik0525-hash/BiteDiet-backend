package com.aacode.bietdiet.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import com.aacode.bietdiet.models.MealPlan;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    Optional<MealPlan> findByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    void deleteByEmail(String email);
}
