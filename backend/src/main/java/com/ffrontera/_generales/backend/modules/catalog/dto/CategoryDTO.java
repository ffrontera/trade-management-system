package com.ffrontera._generales.backend.modules.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        Long parentId,
        String parentName,

        Boolean active
) {
}
