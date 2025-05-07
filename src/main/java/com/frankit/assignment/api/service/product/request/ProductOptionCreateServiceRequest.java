package com.frankit.assignment.api.service.product.request;

import com.frankit.assignment.domain.product.OptionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductOptionCreateServiceRequest {

    private final String name;
    private final OptionType optionType;
    private final BigDecimal additionalPrice;
    private final List<String> values;

    private ProductOptionCreateServiceRequest(String name, OptionType optionType, BigDecimal additionalPrice, List<String> values) {
        this.name = name;
        this.optionType = optionType;
        this.additionalPrice = additionalPrice;
        this.values = values;
    }

    public static ProductOptionCreateServiceRequest of(String name, OptionType optionType, BigDecimal additionalPrice, List<String> values) {
        return new ProductOptionCreateServiceRequest(name, optionType, additionalPrice, values);
    }

}
