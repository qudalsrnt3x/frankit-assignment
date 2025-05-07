package com.frankit.assignment.api.service.product.strategy;

import com.frankit.assignment.api.service.product.request.ProductOptionCreateServiceRequest;
import com.frankit.assignment.domain.product.OptionType;
import com.frankit.assignment.domain.product.Product;
import com.frankit.assignment.domain.product.ProductOption;

public interface ProductOptionCreateStrategy {

    OptionType getOptionType();

    ProductOption create(Long id, Product product, ProductOptionCreateServiceRequest request);

}
