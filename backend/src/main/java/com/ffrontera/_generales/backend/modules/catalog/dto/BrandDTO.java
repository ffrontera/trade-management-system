package com.ffrontera._generales.backend.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BrandDTO(
        Long id,

        @NotBlank(message = "El nombre de la marca es obligatorio")
        String name,

        String logoUrl
) {
}
