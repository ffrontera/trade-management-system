package com.ffrontera._generales.backend.modules.catalog.repository;

import com.ffrontera._generales.backend.modules.catalog.domain.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {

    Optional<SupplierProduct> findBySupplierIdAndSupplierSku(Long supplierId, String supplierSku);

    List<SupplierProduct> findByProductId(Long productId);
}
