package com.frankit.assignment.api.service.product.strategy;

import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.domain.product.OptionType;
import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOption;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SelectOptionCreateStrategy implements ProductOptionCreateStrategy {

    @Override
    public OptionType getOptionType() {
        return OptionType.SELECT;
    }

    @Override
    public ProductOption create(Long id, Product product, ProductOptionCreateServiceRequest request) {
        List<String> values = request.getValues();
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("선택형 옵션은 옵션값이 필수입니다.");
        }

        return ProductOption.of(
                id,
                request.getName(),
                product,
                OptionType.SELECT,
                values,
                request.getAdditionalPrice() != null ? request.getAdditionalPrice() : BigDecimal.ZERO
        );
    }

}
