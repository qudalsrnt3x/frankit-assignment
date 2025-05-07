package com.frankit.assignment.api.service.product;

import com.frankit.assignment.api.service.product.request.ProductCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
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

}
