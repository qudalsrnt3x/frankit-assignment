package com.frankit.assignment.api.service.product.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductUpdateServiceRequest {

    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal deliveryFee;

    private ProductUpdateServiceRequest(String name, String description, BigDecimal price, BigDecimal deliveryFee) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
    }

    public static ProductUpdateServiceRequest of(String name, String description, BigDecimal price, BigDecimal deliveryFee) {
        return new ProductUpdateServiceRequest(name, description, price, deliveryFee);
    }

}
