package com.frankit.assignment.api.service.product;

import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.api.service.product.response.ProductOptionResponse;
import com.frankit.assignment.domain.common.Snowflake;
import com.frankit.assignment.domain.product.OptionType;
import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOptionRepository;
import com.frankit.assignment.domain.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductOptionServiceTest {

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private Snowflake snowflake;

    @AfterEach
    void tearDown() {
        productOptionRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("특정 상품의 상품 옵션을 등록한다.")
    @Test
    void createProductOption() {
        // given
        Product product = productRepository.save(createProduct());

        String optionName = "색상";
        ProductOptionCreateServiceRequest request = ProductOptionCreateServiceRequest.of(
                optionName,
                OptionType.SELECT,
                BigDecimal.ZERO,
                List.of("빨강", "검정")
        );

        // when
        ProductOptionResponse result = productOptionService.createOption(product.getId(), request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOptionType()).isEqualTo(OptionType.SELECT);
        assertThat(result.getName()).isEqualTo(optionName);
        assertThat(result.getValues()).containsExactly("빨강", "검정");
    }

    @DisplayName("존재하지 않는 상품 ID로 옵션 등록 시 예외가 발생한다.")
    @Test
    void createProductOptionFailsIfNoProduct() {
        // given
        Long invalidProductId = 9999L;

        ProductOptionCreateServiceRequest request = ProductOptionCreateServiceRequest.of(
                "색상",
                OptionType.SELECT,
                BigDecimal.ZERO,
                List.of("빨강", "검정")
        );

        // when, then
        assertThatThrownBy(() -> productOptionService.createOption(invalidProductId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품이 존재하지 않습니다");
    }

    @DisplayName("옵션이 3개를 초과하면 등록에 실패한다.")
    @Test
    void createProductOptionFailsIfMoreThanThree() {
        // given
        Product product = productRepository.save(createProduct());

        List<ProductOptionResponse> optionResponses = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductOptionResponse option = productOptionService.createOption(product.getId(),
                    ProductOptionCreateServiceRequest.of(
                            "옵션" + i,
                            OptionType.INPUT,
                            BigDecimal.valueOf(1000),
                            null
                    )
            );
            optionResponses.add(option);
        }

        assertThat(optionResponses).hasSize(3);

        ProductOptionCreateServiceRequest request = ProductOptionCreateServiceRequest.of(
                "추가 옵션",
                OptionType.INPUT,
                BigDecimal.ZERO,
                null
        );
        Long productId = product.getId();

        // when, then
        assertThatThrownBy(() -> productOptionService.createOption(productId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 옵션은 최대 3개까지 등록할 수 있습니다.");
    }

    @Test
    @DisplayName("SELECT 타입인데 values가 null이면 예외가 발생한다.")
    void createSelectOptionFailsIfValuesIsNull() {
        // given
        Product product = productRepository.save(createProduct());
        Long productId = product.getId();

        ProductOptionCreateServiceRequest request = ProductOptionCreateServiceRequest.of(
                "색상",
                OptionType.SELECT,
                BigDecimal.ZERO,
                null
        );

        // when, then
        assertThatThrownBy(() -> productOptionService.createOption(productId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("선택형 옵션은 옵션값이 필수입니다.");
    }

    private Product createProduct() {
        return Product.create(
                snowflake.nextId(),
                "셔츠",
                "면 셔츠",
                BigDecimal.valueOf(29000),
                BigDecimal.valueOf(3000)
        );
    }

}