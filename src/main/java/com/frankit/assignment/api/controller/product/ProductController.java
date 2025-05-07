package com.frankit.assignment.api.controller.product;

import com.frankit.assignment.api.common.response.PageResponse;
import com.frankit.assignment.api.controller.product.request.ProductCreateRequest;
import com.frankit.assignment.api.service.product.ProductService;
import com.frankit.assignment.api.service.product.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        ProductResponse result = productService.create(request.toServiceDto());
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PageResponse<ProductResponse>> findAll(@RequestParam(defaultValue = "1") long page,
                                     @RequestParam(defaultValue = "10") long size) {
        PageResponse<ProductResponse> result = productService.findAll(page, size);
        return ResponseEntity.ok(result);
    }

}
