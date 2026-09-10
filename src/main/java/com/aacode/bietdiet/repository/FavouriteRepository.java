package com.aacode.bietdiet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.aacode.bietdiet.models.Favourite;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    boolean existsByEmailAndRecipeId(String email, String recipeId);

    List<Favourite> findByEmailOrderByIdDesc(String email);

    @Transactional
    @Modifying
    void deleteByEmailAndRecipeId(String email, String recipeId);

    @Transactional
    @Modifying
    void deleteByEmail(String email);
}