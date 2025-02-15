package com.example.FashionFleet.domain.dto.response.product;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Instant createdAt;
    private ProductCategory product_category;

    @Getter
    @Setter
    public static class ProductCategory {
        private long id;
        private String name;
    }
}
