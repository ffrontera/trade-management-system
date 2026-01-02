package com.ffrontera._generales.backend.modules.catalog.mapper;

import com.ffrontera._generales.backend.modules.catalog.domain.Product;
import com.ffrontera._generales.backend.modules.catalog.domain.ProductImage;
import com.ffrontera._generales.backend.modules.catalog.dto.ProductDTO;
import com.ffrontera._generales.backend.modules.catalog.dto.ProductImageDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null)
            return null;

        List<ProductImageDTO> imagesDTO = product.getImages() == null
                ? Collections.emptyList()
                : product.getImages().stream()
                    .map(image -> new ProductImageDTO(
                        image.getId(), image.getImageUrl(), image.getOrderIndex()))
                    .collect(Collectors.toList());

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSkuInternal(),
                product.getSalePrice(),
                product.getCostPrice(),
                product.getStock(),
                product.getBrand() != null ? product.getBrand().getId() : null,
                product.getBrand() != null ? product.getBrand().getName() : null,
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getActive(),
                imagesDTO
        );
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null)
            return null;

        Product product = new Product();
        product.setId(dto.id());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setSkuInternal(dto.skuInternal());
        product.setSalePrice(dto.salePrice());
        product.setCostPrice(dto.costPrice());
        product.setStock(dto.stock() != null ? dto.stock() : 0);
        product.setActive(dto.active() != null ? dto.active() : true);

        if (dto.images() != null) {
            List<ProductImage> images = dto.images().stream()
                    .map(imageDTO -> {
                        var image = new ProductImage();
                        image.setId(imageDTO.id());
                        image.setImageUrl(imageDTO.imageUrl());
                        image.setOrderIndex(imageDTO.orderIndex());
                        image.setProduct(product);
                        return image;
                    })
                    .collect(Collectors.toList());
            product.setImages(images);
        } else {
            product.setImages(Collections.emptyList());
        }
        return product;
    }
}
