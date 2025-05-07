package com.frankit.assignment.api.service.product.strategy;

import com.frankit.assignment.domain.product.OptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductOptionCreateStrategyFactory {

    private final Map<OptionType, ProductOptionCreateStrategy> strategyMap;

    public ProductOptionCreateStrategyFactory(List<ProductOptionCreateStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        ProductOptionCreateStrategy::getOptionType,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        () -> new EnumMap<>(OptionType.class)
                ));
    }

    public ProductOptionCreateStrategy getStrategy(OptionType optionType) {
        ProductOptionCreateStrategy strategy = strategyMap.get(optionType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 옵션 타입입니다: " + optionType);
        }

        return strategy;
    }

}
