package com.frankit.assignment.api.controller.product.request;

import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.domain.product.OptionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductOptionCreateRequest {

    private String name;
    private OptionType optionType;
    private BigDecimal additionalPrice;
    private List<String> values;

    public ProductOptionCreateServiceRequest toServiceDto() {
        return ProductOptionCreateServiceRequest.of(name, optionType, additionalPrice, values);
    }

}
