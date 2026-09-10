package com.aacode.bietdiet.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aacode.bietdiet.models.Cart;
import com.aacode.bietdiet.repository.CartRepository;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartRepository cartRepository;

    public CartController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // ==========================================
    // GET USER CART
    // ==========================================
    @GetMapping
    public ResponseEntity<?> getCart(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        Optional<Cart> cart = cartRepository.findByEmail(email.trim());
        if (cart.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "email", email.trim(),
                    "items", "[]",
                    "message", "Cart is empty"
            ));
        }

        return ResponseEntity.ok(cart.get());
    }

    // ==========================================
    // SAVE OR UPDATE USER CART
    // ==========================================
    @PostMapping
    public ResponseEntity<?> saveCart(@RequestBody Cart cartRequest) {
        if (cartRequest.getEmail() == null || cartRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        String email = cartRequest.getEmail().trim();
        Optional<Cart> existingCart = cartRepository.findByEmail(email);

        Cart cartToSave;
        if (existingCart.isPresent()) {
            cartToSave = existingCart.get();
            if (cartRequest.getItems() != null) {
                cartToSave.setItems(cartRequest.getItems());
            }
            cartToSave.setUpdatedAt(LocalDateTime.now().toString());
        } else {
            cartToSave = new Cart();
            cartToSave.setEmail(email);
            cartToSave.setItems(cartRequest.getItems() != null ? cartRequest.getItems() : "[]");
            cartToSave.setUpdatedAt(LocalDateTime.now().toString());
        }

        Cart saved = cartRepository.save(cartToSave);
        return ResponseEntity.ok(saved);
    }

    // ==========================================
    // CLEAR USER CART
    // ==========================================
    @DeleteMapping
    public ResponseEntity<?> clearCart(@RequestParam String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        cartRepository.deleteByEmail(email.trim());
        return ResponseEntity.ok(Map.of("message", "Cart cleared successfully"));
    }
}
