package com.frankit.assignment.api.service.product.response;

import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOption;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal deliveryFee;
    private final List<ProductOptionResponse> options;

    @Builder
    private ProductResponse(Long id, String name, String description, BigDecimal price, BigDecimal deliveryFee, List<ProductOptionResponse> options) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.options = options;
    }

    public static ProductResponse of(Product product, List<ProductOptionResponse> options) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .options(options)
                .build();
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .deliveryFee(product.getDeliveryFee())
                .build();
    }

}
