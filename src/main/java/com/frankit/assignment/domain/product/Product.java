package com.frankit.assignment.domain.product;

import com.frankit.assignment.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    private Product(Long id, String name, String description, BigDecimal price, BigDecimal deliveryFee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
    }

    public static Product create(Long id, String name, String description, BigDecimal price, BigDecimal deliveryFee) {
        return new Product(id, name, description, price, deliveryFee);
    }

}
