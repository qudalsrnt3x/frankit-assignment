package com.frankit.assignment.domain.product;

import com.frankit.assignment.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductOption extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionType optionType;

    @ElementCollection
    @CollectionTable(name = "product_option_values", joinColumns = @JoinColumn(name = "option_id"))
    @Column(name = "value")
    private List<String> values; // 선택형 타입일 경우만 사용

    @Column(nullable = false)
    private BigDecimal additionalPrice;

    private ProductOption(Long id, String name, Product product, OptionType optionType, List<String> values, BigDecimal additionalPrice) {
        this.id = id;
        this.name = name;
        this.product = product;
        this.optionType = optionType;
        this.values = values;
        this.additionalPrice = additionalPrice;
    }

    public static ProductOption of(Long id, String name, Product product, OptionType optionType, BigDecimal additionalPrice) {
        return new ProductOption(id, name, product, optionType, null, additionalPrice);
    }

    public static ProductOption of(Long id, String name, Product product, OptionType optionType, List<String> values, BigDecimal additionalPrice) {
        return new ProductOption(id, name, product, optionType, values, additionalPrice);
    }

    public void update(String name, OptionType optionType, BigDecimal additionalPrice) {
        this.name = name;
        this.optionType = optionType;
        this.additionalPrice = additionalPrice;
    }

    public void clearOptionValues() {
        this.values.clear();
    }

    public void addOptionValues(List<String> values) {
        this.values.addAll(values);
    }

}
