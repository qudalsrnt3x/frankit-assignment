package com.frankit.assignment.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            value = "SELECT " +
                    "   product.id, product.name, product.description, product.price, product.delivery_fee, " +
                    "   product.created_at, product.modified_at, product.is_deleted " +
                    "FROM ( " +
                    "   SELECT id FROM product " +
                    "   WHERE is_deleted = false " +
                    "   ORDER BY id DESC " +
                    "   LIMIT :limit OFFSET :offset" +
                    ") t LEFT JOIN product ON t.id = product.id",
            nativeQuery = true
    )
    List<Product> findAll(@Param("offset") long offset,
                          @Param("limit") long limit);

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM ( " +
                    "   SELECT id FROM product " +
                    "   WHERE is_deleted = false " +
                    "   LIMIT :limit" +
                    ") t",
            nativeQuery = true
    )
    long count(@Param("limit") long limit);

    Optional<Product> findByIdAndIsDeletedFalse(Long productId);

}
