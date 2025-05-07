package com.frankit.assignment.api.service.product;

import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductOptionUpdateServiceRequest;
import com.frankit.assignment.api.service.product.response.ProductOptionResponse;
import com.frankit.assignment.api.service.product.strategy.ProductOptionStrategy;
import com.frankit.assignment.api.service.product.strategy.ProductOptionStrategyFactory;
import com.frankit.assignment.domain.common.Snowflake;
import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOption;
import com.frankit.assignment.domain.product.ProductOptionRepository;
import com.frankit.assignment.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final ProductOptionStrategyFactory strategyFactory;

    private final Snowflake snowflake;

    @Transactional
    public ProductOptionResponse createOption(Long productId, ProductOptionCreateServiceRequest request) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다.")
        );

        if (productOptionRepository.countByProductId(productId) >= 3) {
            throw new IllegalArgumentException("상품 옵션은 최대 3개까지 등록할 수 있습니다.");
        }

        ProductOptionStrategy strategy = strategyFactory.getStrategy(request.getOptionType());
        ProductOption option = strategy.create(
                snowflake.nextId(),
                findProduct,
                request
        );
        ProductOption savedOption = productOptionRepository.save(option);

        return ProductOptionResponse.of(savedOption);
    }

    @Transactional
    public ProductOptionResponse update(Long productId, Long optionId, ProductOptionUpdateServiceRequest request) {
        productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다.")
        );

        ProductOption findOption = productOptionRepository.findById(optionId).orElseThrow(
                () -> new IllegalArgumentException("옵션이 존재하지 않습니다.")
        );

        ProductOptionStrategy strategy = strategyFactory.getStrategy(request.getOptionType());
        strategy.update(findOption, request);

        return ProductOptionResponse.of(findOption);
    }

    @Transactional
    public void delete(Long productId, Long optionId) {
        productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다.")
        );

        ProductOption findOption = productOptionRepository.findById(optionId).orElseThrow(
                () -> new IllegalArgumentException("옵션이 존재하지 않습니다.")
        );

        if (!findOption.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("해당 상품에 속한 옵션이 아닙니다.");
        }

        productOptionRepository.delete(findOption);
    }

}
