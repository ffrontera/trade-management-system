package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.common.exception.DuplicateResourceException;
import com.ffrontera._generales.backend.modules.catalog.domain.Brand;
import com.ffrontera._generales.backend.modules.catalog.dto.BrandDTO;
import com.ffrontera._generales.backend.modules.catalog.mapper.BrandMapper;
import com.ffrontera._generales.backend.modules.catalog.repository.BrandRepository;
import com.ffrontera._generales.backend.modules.catalog.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    @Test
    void create_ShouldReturnDto_WhenBrandIsNew() {

        BrandDTO inputDto = new BrandDTO(null, "Sasame", null);
        Brand brandEntity = new Brand(1L, "Sasame", null, true);
        BrandDTO expectedDto = new BrandDTO(1L, "Sasame", null);

        when(brandRepository.existsByName("Sasame")).thenReturn(false);
        when(brandMapper.toEntity(inputDto)).thenReturn(brandEntity);
        when(brandRepository.save(brandEntity)).thenReturn(brandEntity);
        when(brandMapper.toDTO(brandEntity)).thenReturn(expectedDto);

        BrandDTO result = brandService.createBrand(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Sasame", result.name());

        verify(brandRepository, times(1)).save(brandEntity);
    }

    @Test
    void create_ShouldThrowException_WhenNameExists() {

        BrandDTO inputDto = new BrandDTO(null, "Cougar", null);
        when(brandRepository.existsByName("Cougar")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            brandService.createBrand(inputDto);
        });

        verify(brandRepository, never()).save(any());
    }

    @Test
    void delete_ShouldSoftDelete_WhenIdExists() {

        Long brandId = 10L;
        Brand brand = new Brand(brandId, "Shimano", null, true);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenAnswer(invocation -> invocation.getArgument(0));

        brandService.deleteBrand(brandId);

        assertFalse(brand.isActive());
        verify(brandRepository).save(brand);
    }
}