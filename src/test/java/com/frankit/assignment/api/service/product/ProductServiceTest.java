package com.frankit.assignment.api.service.product;

import com.frankit.assignment.api.service.product.request.ProductCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.api.service.product.response.ProductResponse;
import com.frankit.assignment.domain.product.OptionType;
import com.frankit.assignment.domain.product.ProductOptionRepository;
import com.frankit.assignment.domain.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @AfterEach
    void tearDown() {
        productOptionRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("옵션 없는 상품을 등록한다.")
    @Test
    void createProductWithoutOptions() {
        // given
        String name = "기본 셔츠";
        String description = "면 셔츠 설명";

        ProductCreateServiceRequest request = ProductCreateServiceRequest.of(
                name,
                description,
                BigDecimal.valueOf(29000),
                BigDecimal.valueOf(3000),
                null
        );

        // when
        ProductResponse result = productService.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDescription()).isEqualTo(description);
    }

    @DisplayName("옵션이 포함된 상품을 등록한다.")
    @Test
    void createProductWithOptions() {
        // given
        List<ProductOptionCreateServiceRequest> options = List.of(
                ProductOptionCreateServiceRequest.of("색상", OptionType.SELECT, BigDecimal.ZERO, List.of("빨강", "검정")),
                ProductOptionCreateServiceRequest.of("사이즈", OptionType.SELECT, BigDecimal.ZERO, List.of("S", "M", "L"))
        );

        String name = "기본 셔츠";
        String description = "면 셔츠 설명";
        ProductCreateServiceRequest request = ProductCreateServiceRequest.of(
                name,
                description,
                BigDecimal.valueOf(29000),
                BigDecimal.valueOf(3000),
                options
        );

        // when
        ProductResponse result = productService.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getOptions()).hasSize(options.size());
    }

    @DisplayName("상품 옵션이 3개를 초과하면 등록에 실패한다.")
    @Test
    void createProductWithMoreThanThreeOptions() {
        // given
        List<ProductOptionCreateServiceRequest> options = List.of(
                ProductOptionCreateServiceRequest.of("색상", OptionType.SELECT, BigDecimal.ZERO, List.of("빨강", "검정")),
                ProductOptionCreateServiceRequest.of("사이즈", OptionType.SELECT, BigDecimal.ZERO, List.of("S", "M", "L")),
                ProductOptionCreateServiceRequest.of("소재", OptionType.INPUT, BigDecimal.valueOf(1000), null),
                ProductOptionCreateServiceRequest.of("배송", OptionType.SELECT, BigDecimal.ZERO, List.of("일반", "당일"))
        );

        ProductCreateServiceRequest request = ProductCreateServiceRequest.of(
                "초과 상품",
                "옵션이 너무 많음",
                BigDecimal.valueOf(39000),
                BigDecimal.valueOf(3000),
                options
        );

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 옵션은 최대 3개까지 등록할 수 있습니다.");
    }

}