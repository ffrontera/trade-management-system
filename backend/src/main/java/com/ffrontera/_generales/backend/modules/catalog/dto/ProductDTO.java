package com.ffrontera._generales.backend.modules.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
        Long id,

        @NotBlank(message = "El nombre del producto es obligatorio")
        String name,

        String description,

        @NotBlank(message = "El SKU interno es obligatorio")
        String skuInternal,

        @NotNull(message = "El precio de venta es obligatorio")
        @Positive(message = "El precio de venta debe ser un valor positivo")
        BigDecimal salePrice,

        @NotNull(message = "El precio de costo es obligatorio")
        @Positive(message = "El precio de costo debe ser un valor positivo")
        BigDecimal costPrice,

        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        @NotNull(message = "La marca es obligatoria")
        Long brandId,
        String brandName,

        @NotNull(message = "La categoría es obligatoria")
        Long categoryId,
        String categoryName,

        Boolean active,
        List<ProductImageDTO> images
) {
}
