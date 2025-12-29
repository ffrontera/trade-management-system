package com.ffrontera._generales.backend.modules.catalog.mapper;

import com.ffrontera._generales.backend.modules.catalog.domain.Category;
import com.ffrontera._generales.backend.modules.catalog.dto.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category entity) {
        if (entity == null) {
            return null;
        }

        return new CategoryDTO(
                entity.getId(),
                entity.getName(),
                entity.getParentCategory() != null ? entity.getParentCategory().getId() : null,
                entity.getParentCategory() != null ? entity.getParentCategory().getName() : null,
                entity.getActive()
        );
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        category.setId(dto.id());
        category.setName(dto.name());
        category.setActive(dto.active());

        return category;
    }
}
