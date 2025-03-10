package com.example.FashionFleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.FashionFleet.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
