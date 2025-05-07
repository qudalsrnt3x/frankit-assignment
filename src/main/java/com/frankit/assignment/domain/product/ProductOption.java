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

}
