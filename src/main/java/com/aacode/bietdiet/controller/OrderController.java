package com.aacode.bietdiet.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aacode.bietdiet.models.Order;
import com.aacode.bietdiet.repository.OrderRepository;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ==========================================
    // CREATE NEW ORDER
    // ==========================================

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody Order order) {

        if (order.getCustomerEmail() == null || order.getCustomerEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Customer email is required.");
        }

        if (order.getItems() == null || order.getItems().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Order items cannot be empty.");
        }

        if (order.getTotalAmount() == null || order.getTotalAmount() < 0) {
            return ResponseEntity.badRequest().body("Valid total amount is required.");
        }

        // New orders always start as Order Confirmed
        order.setStatus("Order Confirmed");

        // Save current date/time
        if (order.getOrderDate() == null || order.getOrderDate().trim().isEmpty()) {
            order.setOrderDate(
                    LocalDateTime.now().toString()
            );
        }

        Order savedOrder =
                orderRepository.save(order);

        return ResponseEntity.ok(savedOrder);
    }


    // ==========================================
    // GET ORDER BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @PathVariable Long id) {

        Optional<Order> order =
                orderRepository.findById(id);

        if (order.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(order.get());
    }


    // ==========================================
    // GET ALL ORDERS OF A USER
    // ==========================================

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders(
            @RequestParam String email) {

        List<Order> orders =
                orderRepository
                        .findByCustomerEmailOrderByIdDesc(email);

        return ResponseEntity.ok(orders);
    }


    // ==========================================
    // UPDATE ORDER STATUS
    // ==========================================

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Optional<Order> optionalOrder =
                orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        Order order = optionalOrder.get();

        order.setStatus(status);

        Order updatedOrder =
                orderRepository.save(order);

        return ResponseEntity.ok(updatedOrder);
    }
}