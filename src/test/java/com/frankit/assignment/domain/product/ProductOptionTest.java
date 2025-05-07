package com.frankit.assignment.domain.product;

import com.frankit.assignment.domain.common.Snowflake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductOptionTest {

    private final Snowflake snowflake = new Snowflake();

    @Test
    @DisplayName("입력형 옵션이 정상적으로 생성된다")
    void createInputTypeOption() {
        // given
        Product product = createProduct();

        // when
        ProductOption option = ProductOption.of(
                snowflake.nextId(),
                "색상",
                product,
                OptionType.INPUT,
                BigDecimal.ZERO
        );

        // then
        assertThat(option.getOptionType()).isEqualTo(OptionType.INPUT);
        assertThat(option.getValues()).isNull();
        assertThat(option.getName()).isEqualTo("색상");
        assertThat(option.getProduct()).isSameAs(product);
    }

    @Test
    @DisplayName("선택형 옵션이 정상적으로 생성된다")
    void createSelectTypeOption() {
        // given
        Product product = createProduct();

        List<String> values = List.of("Small", "Medium", "Large");

        // when
        ProductOption option = ProductOption.of(
                snowflake.nextId(),
                "사이즈",
                product,
                OptionType.SELECT,
                values,
                BigDecimal.valueOf(1000)
        );

        // then
        assertThat(option.getOptionType()).isEqualTo(OptionType.SELECT);
        assertThat(option.getValues()).containsExactly("Small", "Medium", "Large");
        assertThat(option.getAdditionalPrice()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(option.getProduct()).isSameAs(product);
    }

    private Product createProduct() {
        return Product.create(
                snowflake.nextId(),
                "테스트 상품",
                "상품 설명",
                BigDecimal.valueOf(10000),
                BigDecimal.valueOf(2500));
    }

}
