package com.example.FashionFleet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.FashionFleet.domain.ProductInventory;
import com.example.FashionFleet.repository.ProductInventoryRepository;

@Service
public class ProductInventoryService {
    private final ProductInventoryRepository ProductInventoryRepository;

    public ProductInventoryService(ProductInventoryRepository ProductInventoryRepository) {
        this.ProductInventoryRepository = ProductInventoryRepository;
    }

    public boolean isNameExist(String name) {
        return this.ProductInventoryRepository.existsByName(name);
    }

    public ProductInventory handleCreateProductInventory(ProductInventory ProductInventory) {
        return this.ProductInventoryRepository.save(ProductInventory);
    }

    public ProductInventory fetchCategoryById(long id) {
        Optional<ProductInventory> op = this.ProductInventoryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    public ProductInventory handleUpdateProductInventory(ProductInventory ProductInventory) {
        ProductInventory ProductInventoryDB = this.fetchCategoryById(ProductInventory.getId());
        if (ProductInventoryDB != null) {
            ProductInventoryDB.setQuantity(ProductInventory.getQuantity());
            ProductInventoryDB = this.ProductInventoryRepository.save(ProductInventoryDB);
        }
        return ProductInventoryDB;
    }

    public List<ProductInventory> fetchAllCategories() {
        return this.ProductInventoryRepository.findAll();
    }

    public void handleDeleteCategory(long id) {
        this.ProductInventoryRepository.deleteById(id);
    }
}
