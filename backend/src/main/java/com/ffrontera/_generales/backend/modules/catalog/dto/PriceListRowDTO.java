package com.ffrontera._generales.backend.modules.catalog.dto;

import java.math.BigDecimal;

public record PriceListRowDTO(
        String supplierSku,
        String Description,
        BigDecimal newCost
) {
}
