package com.ffrontera._generales.backend.modules.catalog.dto;

public record ProductImageDTO(
        Long id,
        String imageUrl,
        Integer orderIndex
) {
}
