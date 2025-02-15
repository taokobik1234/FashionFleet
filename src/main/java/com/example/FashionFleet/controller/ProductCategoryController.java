package com.example.FashionFleet.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.FashionFleet.domain.ProductCategory;
import com.example.FashionFleet.service.ProductCategoryService;
import com.example.FashionFleet.util.annotation.ApiMessage;
import com.example.FashionFleet.util.error.IdInvalidException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @PostMapping("product-categories")
    @ApiMessage("create new categories")
    public ResponseEntity<ProductCategory> createNewProductCategory(@Valid @RequestBody ProductCategory category)
            throws IdInvalidException {
        boolean isNameExist = this.productCategoryService.isNameExist(category.getName());
        if (isNameExist) {
            throw new IdInvalidException("Category " + category.getName() + " already exist");
        }
        ProductCategory newProductCategory = this.productCategoryService.handleCreateProductCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProductCategory);
    }

    @PutMapping("product-categories")
    @ApiMessage("update categories")
    public ResponseEntity<ProductCategory> updateProductCategory(@Valid @RequestBody ProductCategory category)
            throws IdInvalidException {
        Optional<ProductCategory> isCategoryExist = this.productCategoryService.fetchCategoryById(category.getId());
        if (!isCategoryExist.isPresent()) {
            throw new IdInvalidException("Category " + category.getName() + " does not exist");
        }
        ProductCategory newProductCategory = this.productCategoryService.handleUpdateProductCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProductCategory);
    }

    @GetMapping("product-categories")
    @ApiMessage("fetch all categories")
    public ResponseEntity<List<ProductCategory>> fetchAllCategories() {
        List<ProductCategory> listCategories = this.productCategoryService.fetchAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(listCategories);
    }

    @DeleteMapping("product-categories/{id}")
    @ApiMessage("delete cate by id")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") long id) throws IdInvalidException {
        Optional<ProductCategory> isCategoryExist = this.productCategoryService.fetchCategoryById(id);
        if (!isCategoryExist.isPresent()) {
            throw new IdInvalidException("Category " + id + " does not exist");
        }
        this.productCategoryService.handleDeleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
