package com.frankit.assignment.domain.product;

import com.frankit.assignment.domain.common.Snowflake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private final Snowflake snowflake = new Snowflake();

    @DisplayName("Product 객체가 정상 생성된다.")
    @Test
    void test() {
        // given
        long generatedId = snowflake.nextId();
        String name = "테스트 상품";
        String description = "테스트용 상품 설명";
        BigDecimal price = BigDecimal.valueOf(10000);
        BigDecimal deliveryFee = BigDecimal.valueOf(2500);

        // when
        Product product = Product.create(generatedId, name, description, price, deliveryFee);

        // then
        assertEquals(generatedId, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(deliveryFee, product.getDeliveryFee());
    }

}