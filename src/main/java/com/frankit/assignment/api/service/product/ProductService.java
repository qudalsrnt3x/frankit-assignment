package com.frankit.assignment.api.service.product;

import com.frankit.assignment.api.common.response.PageResponse;
import com.frankit.assignment.api.service.PageLimitCalculator;
import com.frankit.assignment.api.service.product.request.ProductCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductUpdateServiceRequest;
import com.frankit.assignment.api.service.product.response.ProductOptionResponse;
import com.frankit.assignment.api.service.product.response.ProductResponse;
import com.frankit.assignment.domain.common.Snowflake;
import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final Snowflake snowflake;

    public PageResponse<ProductResponse> findAll(long page, long size) {
        return PageResponse.of(
                productRepository.findAll((page - 1) * size, size).stream()
                        .map(ProductResponse::of)
                        .toList(),
                productRepository.count(PageLimitCalculator.calculatePageLimit(page, size, 10L))
        );
    }

    public ProductResponse findById(Long productId) {
        Product findProduct = productRepository.findByIdAndIsDeletedFalse(productId).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다.")
        );
        return ProductResponse.of(findProduct);
    }

    @Transactional
    public ProductResponse create(ProductCreateServiceRequest request) {
        if (request.getOptions() != null && request.getOptions().size() > 3) {
            throw new IllegalArgumentException("상품 옵션은 최대 3개까지 등록할 수 있습니다.");
        }

        Product createProduct = Product.create(
                snowflake.nextId(),
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getDeliveryFee()
        );
        Product savedProduct = productRepository.save(createProduct);

        List<ProductOptionCreateServiceRequest> options = request.getOptions();
        if (options != null && !options.isEmpty()) {
            List<ProductOptionResponse> optionList = new ArrayList<>();
            options.forEach(optionsReq -> {
                ProductOptionResponse option = productOptionService.createOption(savedProduct.getId(), optionsReq);
                optionList.add(option);
            });
            return ProductResponse.of(savedProduct, optionList);
        }

        return ProductResponse.of(savedProduct);
    }

    @Transactional
    public ProductResponse update(Long productId, ProductUpdateServiceRequest request) {
        Product findProduct = getProduct(productId);

        findProduct.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getDeliveryFee()
        );

        return ProductResponse.of(findProduct);
    }

    @Transactional
    public void delete(Long productId) {
        Product findProduct = getProduct(productId);
        findProduct.softDelete();
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품이 존재하지 않습니다.")
        );
    }

}
