package com.ffrontera._generales.backend.modules.suppliers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SupplierDTO(
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "El CUIT es obligatorio")
        String code,

        @Email(message = "El email no es válido" )
        String email,

        String phone,
        String address,

        Boolean active
) {
}
