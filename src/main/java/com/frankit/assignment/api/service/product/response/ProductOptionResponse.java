package com.frankit.assignment.api.service.product.response;

import com.frankit.assignment.domain.product.OptionType;
import com.frankit.assignment.domain.product.ProductOption;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductOptionResponse {

    private final Long id;
    private final String name;
    private final OptionType optionType;
    private final List<String> values;
    private final BigDecimal additionalPrice;

    @Builder
    private ProductOptionResponse(Long id, String name, OptionType optionType, List<String> values, BigDecimal additionalPrice) {
        this.id = id;
        this.name = name;
        this.optionType = optionType;
        this.values = values;
        this.additionalPrice = additionalPrice;
    }

    public static ProductOptionResponse of(ProductOption productOption) {
        return ProductOptionResponse.builder()
                .id(productOption.getId())
                .name(productOption.getName())
                .optionType(productOption.getOptionType())
                .values(productOption.getValues())
                .additionalPrice(productOption.getAdditionalPrice())
                .build();
    }

}
