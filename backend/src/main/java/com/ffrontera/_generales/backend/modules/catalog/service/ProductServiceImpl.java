package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.common.exception.DuplicateResourceException;
import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.catalog.domain.Product;
import com.ffrontera._generales.backend.modules.catalog.domain.ProductImage;
import com.ffrontera._generales.backend.modules.catalog.dto.ProductDTO;
import com.ffrontera._generales.backend.modules.catalog.mapper.ProductMapper;
import com.ffrontera._generales.backend.modules.catalog.repository.BrandRepository;
import com.ffrontera._generales.backend.modules.catalog.repository.CategoryRepository;
import com.ffrontera._generales.backend.modules.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productRepository.existsBySkuInternal(productDTO.skuInternal()))
            throw new DuplicateResourceException("Producto con SKU interno '" + productDTO.skuInternal() + "' ya existe.");

        var product = productMapper.toEntity(productDTO);
        assignRelations(product, productDTO.brandId(), productDTO.categoryId());

        return productMapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + productId + " no encontrado."));

        if (!product.getSkuInternal().equals(productDTO.skuInternal()) && productRepository.existsBySkuInternal(productDTO.skuInternal())) {
            throw new DuplicateResourceException("Producto con SKU interno '" + productDTO.skuInternal() + "' ya existe.");
        }

        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setSkuInternal(productDTO.skuInternal());
        product.setSalePrice(productDTO.salePrice());
        product.setCostPrice(productDTO.costPrice());
        product.setStock(productDTO.stock() != null ? productDTO.stock() : product.getStock());
        product.setActive(productDTO.active() != null ? productDTO.active() : product.getActive());

        assignRelations(product, productDTO.brandId(), productDTO.categoryId());

        if (productDTO.images() != null) {
            product.getImages().clear();
            productDTO.images().forEach(imageDTO -> {
                var image = new ProductImage();
                image.setImageUrl(imageDTO.imageUrl());
                image.setOrderIndex(imageDTO.orderIndex());
                image.setProduct(product);
                product.getImages().add(image);
            });
        }

        return productMapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAll(int page, int size, String sortBy, String sortDir,
                                   String name, Long brandId, Long categoryId, Boolean active) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.search(name, brandId, categoryId, active, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto con ID " + productId + " no encontrado."));

        product.setActive(false);
        productRepository.save(product);
    }

    private void assignRelations(Product product, Long brandId, Long categoryId) {
        if (brandId != null) {
            var brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ResourceNotFoundException("Marca con ID " + brandId + " no encontrada."));
            product.setBrand(brand);
        }

        if (categoryId != null) {
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría con ID " + categoryId + " no encontrada."));
            product.setCategory(category);
        }
    }
}
