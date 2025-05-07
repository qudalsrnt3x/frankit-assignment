package com.frankit.assignment.api.controller.product.request;

import com.frankit.assignment.api.service.product.request.ProductCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductCreateRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal deliveryFee;
    private List<ProductOptionCreateRequest> options;

    @Builder
    private ProductCreateRequest(String name, String description, BigDecimal price, BigDecimal deliveryFee, List<ProductOptionCreateRequest> options) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.options = options;
    }

    public ProductCreateServiceRequest toServiceDto() {
        List<ProductOptionCreateServiceRequest> optionRequest =
                options == null ? null
                        : options.stream()
                        .map(ProductOptionCreateRequest::toServiceDto)
                        .toList();
        return ProductCreateServiceRequest.of(name, description, price, deliveryFee, optionRequest);
    }

}
