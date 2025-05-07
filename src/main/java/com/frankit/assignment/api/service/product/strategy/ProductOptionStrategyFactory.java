package com.frankit.assignment.api.service.product.strategy;

import com.frankit.assignment.domain.product.OptionType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductOptionStrategyFactory {

    private final Map<OptionType, ProductOptionStrategy> strategyMap;

    public ProductOptionStrategyFactory(List<ProductOptionStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        ProductOptionStrategy::getOptionType,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        () -> new EnumMap<>(OptionType.class)
                ));
    }

    public ProductOptionStrategy getStrategy(OptionType optionType) {
        ProductOptionStrategy strategy = strategyMap.get(optionType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 옵션 타입입니다: " + optionType);
        }

        return strategy;
    }

}
