package com.frankit.assignment.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    int countByProductId(Long productId);
}
