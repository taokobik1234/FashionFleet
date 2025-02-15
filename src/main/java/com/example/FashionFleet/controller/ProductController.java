package com.example.FashionFleet.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.FashionFleet.domain.Product;
import com.example.FashionFleet.domain.ProductCategory;
import com.example.FashionFleet.domain.dto.ResultPaginationDTO;
import com.example.FashionFleet.domain.dto.response.product.ResCreateProductDTO;
import com.example.FashionFleet.domain.dto.response.product.ResUpdateProductDTO;
import com.example.FashionFleet.service.ProductCategoryService;
import com.example.FashionFleet.service.ProductService;
import com.example.FashionFleet.util.annotation.ApiMessage;
import com.example.FashionFleet.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;

@RestController
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public ProductController(ProductService productService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @PostMapping("/products")
    @ApiMessage("create new product")
    public ResponseEntity<ResCreateProductDTO> createNewProduct(@Valid @RequestBody Product product)
            throws IdInvalidException {
        Optional<ProductCategory> isCategoryExist = this.productCategoryService
                .fetchCategoryById(product.getProduct_category().getId());
        if (!isCategoryExist.isPresent()) {
            throw new IdInvalidException("Category " + product.getProduct_category().getId() + " does not exist");
        }
        Product newProduct = this.productService.handleCreateProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.productService.convertToResCreatProductDTO(newProduct));
    }

    @PutMapping("/products")
    @ApiMessage("Update a product")
    public ResponseEntity<ResUpdateProductDTO> updateAProduct(@Valid @RequestBody Product product)
            throws IdInvalidException {
        Product updateProduct = this.productService.handleUpdateProduct(product);
        if (updateProduct == null) {
            throw new IdInvalidException("Product " + product.getId() + " does not exists");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.productService.convertToResUpdateProductDTO(updateProduct));
    }

    @GetMapping("/products/{id}")
    @ApiMessage("Fetch product by id")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) throws IdInvalidException {
        Product fetchProduct = this.productService.fetchProductById(id);
        if (fetchProduct == null) {
            throw new IdInvalidException("Product " + id + " does not exists");
        }
        return ResponseEntity.ok().body(fetchProduct);
    }

    @GetMapping("/products")
    @ApiMessage("Fetch all products")
    public ResponseEntity<ResultPaginationDTO> getAllProducts(@Filter Specification<Product> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.fetchALlProduct(spec, pageable));
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") long id) throws IdInvalidException {
        Product product = this.productService.fetchProductById(id);
        if (product == null) {
            throw new IdInvalidException("Product with id " + id + " does not exist");
        }
        this.productService.handleDeleteProduct(id);
        return ResponseEntity.ok().body(null);
    }
}
