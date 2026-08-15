package com.aacode.bietdiet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aacode.bietdiet.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerEmailOrderByIdDesc(String customerEmail);
}