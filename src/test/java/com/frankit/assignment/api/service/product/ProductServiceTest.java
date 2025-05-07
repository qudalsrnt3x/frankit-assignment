package com.frankit.assignment.api.service.product;

import com.frankit.assignment.api.common.response.PageResponse;
import com.frankit.assignment.api.service.product.request.ProductCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.api.service.product.request.ProductUpdateServiceRequest;
import com.frankit.assignment.api.service.product.response.ProductResponse;
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

    @DisplayName("상품 목록 조회 시 정상적으로 데이터를 페이징으로 응답한다.")
    @Test
    void findAll() {
        // given
        for (int i = 1; i <= 5; i++) {
            Product product = Product.create(
                    (long) i,
                    "상품" + i,
                    "설명" + i,
                    BigDecimal.valueOf(10000 * i),
                    BigDecimal.valueOf(3000)
            );
            productRepository.save(product);
        }

        // when
        PageResponse<ProductResponse> result = productService.findAll(1, 3);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(3);
        assertThat(result.getItemCount()).isGreaterThanOrEqualTo(3);
    }

    @DisplayName("상품 ID를 통해 상품 정보를 단건으로 조회한다.")
    @Test
    void findById() {
        // given
        Product product = productRepository.save(Product.create(1L, "상품A", "설명A", BigDecimal.valueOf(1000), BigDecimal.valueOf(3000)));

        // when
        ProductResponse result = productService.findById(product.getId());

        // then
        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo("상품A");
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

    @DisplayName("특정 상품을 수정한다.")
    @Test
    void updateProduct() {
        // given
        Product product = productRepository.save(Product.create(1L, "상품A", "설명A", BigDecimal.valueOf(1000), BigDecimal.valueOf(3000)));

        ProductUpdateServiceRequest request = ProductUpdateServiceRequest.of(
                "수정된상품",
                "수정된설명",
                BigDecimal.valueOf(2000),
                BigDecimal.valueOf(4000)
        );

        // when
        ProductResponse result = productService.update(product.getId(), request);

        // then
        assertThat(result.getName()).isEqualTo("수정된상품");
        assertThat(result.getDescription()).isEqualTo("수정된설명");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(2000));
        assertThat(result.getDeliveryFee()).isEqualTo(BigDecimal.valueOf(4000));
    }

    @DisplayName("상품 삭제 후 조회 시 예외가 발생한다.")
    @Test
    void deleteProduct() {
        // given
        Product product = productRepository.save(Product.create(1L, "상품A", "설명A", BigDecimal.valueOf(1000), BigDecimal.valueOf(3000)));
        Long productId = product.getId();

        // when
        productService.delete(product.getId());

        // then
        assertThatThrownBy(() -> productService.findById(productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품이 존재하지 않습니다.");
    }

}