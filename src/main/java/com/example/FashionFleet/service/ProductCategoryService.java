package com.example.FashionFleet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.FashionFleet.domain.ProductCategory;
import com.example.FashionFleet.repository.ProductCategoryRepository;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public boolean isNameExist(String name) {
        return this.productCategoryRepository.existsByName(name);
    }

    public ProductCategory handleCreateProductCategory(ProductCategory productCategory) {
        return this.productCategoryRepository.save(productCategory);
    }

    public ProductCategory fetchCategoryById(long id) {
        Optional<ProductCategory> op = this.productCategoryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    public ProductCategory handleUpdateProductCategory(ProductCategory productCategory) {
        ProductCategory productCategoryDB = this.fetchCategoryById(productCategory.getId());
        if (productCategoryDB != null) {
            productCategoryDB.setName(productCategory.getName());
            productCategoryDB.setDescription(productCategory.getDescription());
            productCategoryDB = this.productCategoryRepository.save(productCategoryDB);
        }
        return productCategoryDB;
    }

    public List<ProductCategory> fetchAllCategories() {
        return this.productCategoryRepository.findAll();
    }

    public void handleDeleteCategory(long id) {
        this.productCategoryRepository.deleteById(id);
    }
}
