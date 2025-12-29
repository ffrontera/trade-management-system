package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.common.exception.DuplicateResourceException;
import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.catalog.dto.CategoryDTO;
import com.ffrontera._generales.backend.modules.catalog.mapper.CategoryMapper;
import com.ffrontera._generales.backend.modules.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.name())) {
            throw new DuplicateResourceException("Category with name '" + categoryDTO.name() + "' already exists.");
        }

        var category = categoryMapper.toEntity(categoryDTO);
        category.setActive(true);

        if (categoryDTO.parentId() != null) {
            var parentCategory = categoryRepository.findById(categoryDTO.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category with id '" + categoryDTO.parentId() + "' not found."));
            category.setParentCategory(parentCategory);
        }

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id '" + id + "' not found."));

        category.setName(categoryDTO.name());

        if (categoryDTO.parentId() != null) {

            if (categoryDTO.parentId().equals(id))
                throw new IllegalArgumentException("Una categoría no puede ser padre de sí misma");

            var newParent = categoryRepository.findById(categoryDTO.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category with id '" + categoryDTO.parentId() + "' not found."));
        } else {
            category.setParentCategory(null);
        }

        return categoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> getCategories(int page, int size, String sortBy, String sortDir, String name, Boolean active) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return categoryRepository.search(name, active, pageable)
                .map(categoryMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id '" + id + "' not found."));
        category.setActive(false);
        categoryRepository.save(category);
    }
}
