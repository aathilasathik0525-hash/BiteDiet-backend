package com.aacode.bietdiet.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import com.aacode.bietdiet.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    void deleteByEmail(String email);
}
