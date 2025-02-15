package com.example.FashionFleet.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.FashionFleet.domain.Product;
import com.example.FashionFleet.domain.ProductCategory;
import com.example.FashionFleet.domain.dto.ResultPaginationDTO;
import com.example.FashionFleet.domain.dto.response.product.ResCreateProductDTO;
import com.example.FashionFleet.domain.dto.response.product.ResUpdateProductDTO;
import com.example.FashionFleet.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;

    public ProductService(ProductRepository productRepository, ProductCategoryService productCategoryService) {
        this.productRepository = productRepository;
        this.productCategoryService = productCategoryService;
    }

    public Product fetchProductById(long id) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        return null;
    }

    public Product handleCreateProduct(Product product) {
        if (product.getProduct_category() != null) {
            Optional<ProductCategory> categoryOptional = this.productCategoryService
                    .fetchCategoryById(product.getProduct_category().getId());
            product.setProduct_category(categoryOptional.isPresent() ? categoryOptional.get() : null);
        }
        return this.productRepository.save(product);
    }

    public ResCreateProductDTO convertToResCreatProductDTO(Product product) {
        ResCreateProductDTO res = new ResCreateProductDTO();
        ResCreateProductDTO.ProductCategory cate = new ResCreateProductDTO.ProductCategory();

        res.setId(product.getId());
        res.setDescription(product.getDescription());
        res.setName(product.getName());
        res.setPrice(product.getPrice());
        res.setCreatedAt(product.getCreatedAt());
        if (product.getProduct_category() != null) {
            cate.setId(product.getProduct_category().getId());
            cate.setName(product.getProduct_category().getName());
            res.setProduct_category(cate);
        }
        return res;
    }

    public Product handleUpdateProduct(Product product) {
        Product productDB = this.fetchProductById(product.getId());
        if (productDB != null) {
            productDB.setDescription(product.getDescription());
            productDB.setName(product.getName());
            productDB.setPrice(product.getPrice());
            if (product.getProduct_category() != null) {
                Optional<ProductCategory> cateOptional = this.productCategoryService
                        .fetchCategoryById(product.getProduct_category().getId());
                productDB.setProduct_category(cateOptional.isPresent() ? cateOptional.get() : null);
            }
            productDB = this.productRepository.save(productDB);
        }
        return productDB;
    }

    public ResUpdateProductDTO convertToResUpdateProductDTO(Product product) {
        ResUpdateProductDTO res = new ResUpdateProductDTO();
        ResUpdateProductDTO.ProductCategory cate = new ResUpdateProductDTO.ProductCategory();

        res.setId(product.getId());
        res.setDescription(product.getDescription());
        res.setName(product.getName());
        res.setPrice(product.getPrice());
        res.setUpdatedAt(product.getUpdatedAt());
        if (product.getProduct_category() != null) {
            cate.setId(product.getProduct_category().getId());
            cate.setName(product.getProduct_category().getName());
            res.setProduct_category(cate);
        }
        return res;
    }

    public ResultPaginationDTO fetchALlProduct(Specification<Product> spec, Pageable pageable) {
        Page<Product> pageProduct = this.productRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageProduct.getTotalPages());
        mt.setTotal(pageProduct.getTotalElements());

        res.setMeta(mt);

        List<ResCreateProductDTO> listProduct = pageProduct.getContent().stream()
                .map(item -> this.convertToResCreatProductDTO(item)).collect(Collectors.toList());
        res.setResult(listProduct);
        return res;
    }

    public void handleDeleteProduct(long id) {
        this.productRepository.deleteById(id);
    }
}
