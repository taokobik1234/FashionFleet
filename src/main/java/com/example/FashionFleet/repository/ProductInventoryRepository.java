package com.example.FashionFleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.FashionFleet.domain.ProductInventory;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    boolean existsByName(String name);
}
