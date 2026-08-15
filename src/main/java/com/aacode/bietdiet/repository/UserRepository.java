package com.aacode.bietdiet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aacode.bietdiet.models.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);
}