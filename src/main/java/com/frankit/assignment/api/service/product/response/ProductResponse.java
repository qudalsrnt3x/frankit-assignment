package com.frankit.assignment.api.service.product.response;

import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOption;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProductResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal deliveryFee;
    private final List<ProductOptionResponse> options;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    @Builder
    private ProductResponse(Long id, String name, String description, BigDecimal price, BigDecimal deliveryFee, List<ProductOptionResponse> options, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.options = options;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ProductResponse of(Product product, List<ProductOptionResponse> options) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .options(options)
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();
    }

}
