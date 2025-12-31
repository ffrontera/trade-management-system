package com.ffrontera._generales.backend.modules.catalog.repository;

import com.ffrontera._generales.backend.modules.catalog.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySkuInternal(String skuInternal);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR :name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:brandId IS NULL OR p.brand.id = :brandId) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:active IS NULL OR p.active = :active)")
    Page<Product> search(String name, Long brandId, Long categoryId, Boolean active, Pageable pageable);
}
