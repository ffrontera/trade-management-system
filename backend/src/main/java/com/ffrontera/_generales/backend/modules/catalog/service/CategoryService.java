package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.modules.catalog.dto.CategoryDTO;
import org.springframework.data.domain.Page;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    Page<CategoryDTO> getCategories(int page, int size, String sortBy, String sortDir, String name, Boolean active);
    void deleteCategory(Long id);
}
