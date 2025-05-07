package com.frankit.assignment.api.service.product.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductCreateServiceRequest {

    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal deliveryFee;

    private final List<ProductOptionCreateServiceRequest> options;

    private ProductCreateServiceRequest(String name, String description, BigDecimal price, BigDecimal deliveryFee, List<ProductOptionCreateServiceRequest> options) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.options = options;
    }

    public static ProductCreateServiceRequest of(String name, String description, BigDecimal price, BigDecimal deliveryFee, List<ProductOptionCreateServiceRequest> options) {
        return new ProductCreateServiceRequest(name, description, price, deliveryFee, options);
    }

}
