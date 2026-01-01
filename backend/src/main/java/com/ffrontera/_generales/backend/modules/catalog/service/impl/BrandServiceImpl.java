package com.ffrontera._generales.backend.modules.catalog.service.impl;

import com.ffrontera._generales.backend.common.exception.DuplicateResourceException;
import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.catalog.dto.BrandDTO;
import com.ffrontera._generales.backend.modules.catalog.repository.BrandRepository;
import com.ffrontera._generales.backend.modules.catalog.mapper.BrandMapper;
import com.ffrontera._generales.backend.modules.catalog.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @Transactional
    public BrandDTO createBrand(BrandDTO brandDTO) {

        if (brandRepository.existsByName(brandDTO.name()))
            throw new DuplicateResourceException("La marca con el nombre '" + brandDTO.name() + "' ya existe.");

        var brand = brandMapper.toEntity(brandDTO);
        brand.setActive(true);

        return brandMapper.toDTO(brandRepository.save(brand));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandDTO> getBrands(int page, int size, String sortBy, String sortDir, String name, Boolean active) {

        var sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return brandRepository.search(name, active, pageable)
                .map(brandMapper::toDTO);
    }

    @Override
    @Transactional
    public BrandDTO updateBrand(Long id, BrandDTO brandDTO) {
        if (brandRepository.existsByNameAndIdNot(brandDTO.name(), id))
            throw new DuplicateResourceException("Otra marca con el nombre '" + brandDTO.name() + "' ya existe.");

        var brandOpt = brandRepository.findById(id);

        if (brandOpt.isPresent()) {

            var brand = brandOpt.get();

            if (brandDTO.name() != null)
                brand.setName(brandDTO.name());
            if (brandDTO.logoUrl() != null)
                brand.setLogoUrl(brandDTO.logoUrl());

            return brandMapper.toDTO(brandRepository.save(brand));
        }
        else {
            throw new ResourceNotFoundException("La marca con id '" + id + "' no existe.");
        }
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        var searchBrand = brandRepository.findById(id);

        if (!searchBrand.isPresent())
            throw new ResourceNotFoundException("La marca con id '" + id + "' no existe.");

        var brand = searchBrand.get();
        brand.setActive(false);
        brandRepository.save(brand);
    }
}
