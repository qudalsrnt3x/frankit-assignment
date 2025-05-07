package com.frankit.assignment.api.service.product.strategy;

import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.domain.product.OptionType;
import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOption;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InputOptionCreateStrategy implements ProductOptionCreateStrategy {

    @Override
    public OptionType getOptionType() {
        return OptionType.INPUT;
    }

    @Override
    public ProductOption create(Long id, Product product, ProductOptionCreateServiceRequest request) {
        return ProductOption.of(
                id,
                request.getName(),
                product,
                OptionType.INPUT,
                null,
                request.getAdditionalPrice() != null ? request.getAdditionalPrice() : BigDecimal.ZERO
        );
    }

}
