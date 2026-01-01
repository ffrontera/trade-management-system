package com.ffrontera._generales.backend.modules.catalog.domain;

import com.ffrontera._generales.backend.modules.suppliers.domain.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"supplier_id", "product_sku"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private String supplierSku;

    private String supplierProductName;

    @Column(nullable = false)
    private BigDecimal costPrice;

    @Column(nullable = false)
    private String currency;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
