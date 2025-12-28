package com.ffrontera._generales.backend.modules.catalog.service.mapper;

import com.ffrontera._generales.backend.modules.catalog.domain.Brand;
import com.ffrontera._generales.backend.modules.catalog.dto.BrandDTO;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {
    public Brand toEntity(BrandDTO dto) {
        return Brand.builder()
                .id(dto.id())
                .name(dto.name())
                .logoUrl(dto.logoUrl())
                .build();
    }

    public BrandDTO toDTO(Brand entity) {
        return BrandDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .logoUrl(entity.getLogoUrl())
                .build();
    }
}
