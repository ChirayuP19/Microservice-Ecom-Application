package com.ecommerce.product.repository;


import com.ecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.stockQuantity > 0 AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))" )
    List<Product> searchProducts(@Param("keyword") String keyword);

     Optional<Product> findByIdAndActiveTrue(Long id);
}
