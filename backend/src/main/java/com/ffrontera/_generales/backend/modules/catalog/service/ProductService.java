package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.modules.catalog.dto.ProductDTO;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
    Page<ProductDTO> getAll(int page, int size, String sortBy, String sortDir,
                            String name, Long brandId, Long categoryId, Boolean active);
    void deleteProduct(Long productId);
}
