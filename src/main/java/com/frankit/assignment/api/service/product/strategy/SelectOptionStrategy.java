package com.frankit.assignment.api.service.product.strategy;

import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductOptionUpdateServiceRequest;
import com.frankit.assignment.domain.product.OptionType;
import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOption;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SelectOptionStrategy implements ProductOptionStrategy {

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

    @Override
    public void update(ProductOption option, ProductOptionUpdateServiceRequest request) {
        if (request.getValues() == null || request.getValues().isEmpty()) {
            throw new IllegalArgumentException("선택형 옵션에는 값 목록이 필요합니다.");
        }

        option.update(request.getName(), request.getOptionType(), request.getAdditionalPrice());
        option.clearOptionValues();
        option.addOptionValues(request.getValues());
    }

}
