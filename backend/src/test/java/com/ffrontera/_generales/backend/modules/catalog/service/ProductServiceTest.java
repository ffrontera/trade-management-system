package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.common.exception.DuplicateResourceException;
import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.catalog.domain.Brand;
import com.ffrontera._generales.backend.modules.catalog.domain.Category;
import com.ffrontera._generales.backend.modules.catalog.domain.Product;
import com.ffrontera._generales.backend.modules.catalog.domain.ProductImage;
import com.ffrontera._generales.backend.modules.catalog.dto.ProductDTO;
import com.ffrontera._generales.backend.modules.catalog.dto.ProductImageDTO;
import com.ffrontera._generales.backend.modules.catalog.mapper.ProductMapper;
import com.ffrontera._generales.backend.modules.catalog.repository.BrandRepository;
import com.ffrontera._generales.backend.modules.catalog.repository.CategoryRepository;
import com.ffrontera._generales.backend.modules.catalog.repository.ProductRepository;
import com.ffrontera._generales.backend.modules.catalog.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void create_ShouldSaveProduct_WhenDataIdCorrect() {
        Long brandId = 1L;
        Long categoryId = 1L;

        ProductImageDTO imgDTO = new ProductImageDTO(null, "http://img.com/foto.jpg", 1);
        List<ProductImageDTO> imagesList = List.of(imgDTO);

        ProductDTO inputDTO = new ProductDTO(null, "Product Name", "Product Description", "SKU-100",
                new BigDecimal("100.0"), new BigDecimal("50.0"), 10, brandId, null, categoryId, null, true, imagesList);

        Product productEntity = new Product();
        productEntity.setSkuInternal("SKU-100");

        ProductImage imgEntity = new ProductImage();
        imgEntity.setImageUrl("http://img.com/foto.jpg");
        imgEntity.setProduct(productEntity);
        imgEntity.setOrderIndex(1);
        productEntity.setImages(new ArrayList<>(List.of(imgEntity)));

        when(productRepository.existsBySkuInternal("SKU-100")).thenReturn(false);
        when(productMapper.toEntity(inputDTO)).thenReturn(productEntity);
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(new Brand()));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toDTO(productEntity)).thenReturn(inputDTO);

        ProductDTO result = productService.createProduct(inputDTO);

        assertNotNull(result);
        assertFalse(result.images().isEmpty());
        assertEquals("http://img.com/foto.jpg", result.images().get(0).imageUrl());

        verify(productRepository).save(argThat(
                prod -> prod.getImages().size() == 1 &&
                prod.getImages().get(0).getImageUrl().equals("http://img.com/foto.jpg")
        ));

    }

    @Test
    void create_ShouldThrowException_WhenBrandNotFound() {
        Long brandId = 1L;
        Long categoryId = 1L;

        ProductDTO inputDTO = new ProductDTO(null, "Product Name", "Product Description", "SKU-100",
                new BigDecimal("100.0"), new BigDecimal("50.0"), 10, brandId, null, categoryId, null, true, null);

        Product productEntity = new Product();

        when(productRepository.existsBySkuInternal(any())).thenReturn(false);
        when(productMapper.toEntity(any())).thenReturn(productEntity);

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(inputDTO));
        verify(productRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenSkuExists() {
        ProductDTO inputDTO = new ProductDTO(null, "Product Name", "Product Description", "SKU-100",
                new BigDecimal("100.0"), new BigDecimal("50.0"), 10, 1L, null, 1L, null, true, null);

        when(productRepository.existsBySkuInternal("SKU-100")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> productService.createProduct(inputDTO));
        verify(productRepository, never()).save(any());
    }
}