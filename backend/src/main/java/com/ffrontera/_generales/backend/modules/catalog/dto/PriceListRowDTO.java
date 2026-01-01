package com.ffrontera._generales.backend.modules.catalog.dto;

public record PriceListRowDTO(
        String supplierSku,
        String Description,
        Double newCost
) {
}
