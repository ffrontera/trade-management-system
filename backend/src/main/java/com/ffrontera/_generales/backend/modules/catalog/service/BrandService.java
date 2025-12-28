package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.modules.catalog.dto.BrandDTO;
import org.springframework.data.domain.Page;

public interface BrandService {

    BrandDTO createBrand(BrandDTO brandDTO);
    Page<BrandDTO> getBrands(int page, int size, String sortBy, String sortDir, String name, Boolean active);
    BrandDTO updateBrand(Long id, BrandDTO brandDTO);
    void deleteBrand(Long id);
}
