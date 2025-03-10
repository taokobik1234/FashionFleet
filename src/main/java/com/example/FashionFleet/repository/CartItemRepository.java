package com.example.FashionFleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.FashionFleet.domain.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
