package com.frankit.assignment.api.service.product.response;

import com.frankit.assignment.domain.product.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
                .options(product.getOptions().stream()
                        .map(ProductOptionResponse::of)
                        .toList())
                .build();
    }

}
