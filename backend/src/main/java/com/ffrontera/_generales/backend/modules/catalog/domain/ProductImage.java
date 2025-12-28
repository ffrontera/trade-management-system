package com.ffrontera._generales.backend.modules.catalog.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
