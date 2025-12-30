package com.ffrontera._generales.backend.modules.suppliers.repository;

import com.ffrontera._generales.backend.modules.suppliers.domain.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByCode(String code);
    boolean existsByEmail(String email);

    @Query("SELECT s FROM Supplier s WHERE " +
            "(:query IS NULL OR :query = '' OR " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.code) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "(:active IS NULL OR s.active = :active)")
    Page<Supplier> search(String query, Boolean active, Pageable pageable);
}
