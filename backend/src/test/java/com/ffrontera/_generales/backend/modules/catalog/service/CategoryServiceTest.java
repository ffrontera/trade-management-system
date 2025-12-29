package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.catalog.domain.Category;
import com.ffrontera._generales.backend.modules.catalog.dto.CategoryDTO;
import com.ffrontera._generales.backend.modules.catalog.mapper.CategoryMapper;
import com.ffrontera._generales.backend.modules.catalog.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void create_ShouldSetParent_WhenParentIdProvided() {

        Long parentId = 1L;
        CategoryDTO inputDto = new CategoryDTO(null, "Child Category", parentId, null, true);

        Category parentEntity = new Category();
        parentEntity.setId(parentId);
        parentEntity.setName("Parent Category");

        Category childEntity = new Category();
        childEntity.setName("Child Category");

        Category savedEntity = new Category();
        savedEntity.setId(2L);
        savedEntity.setName("Child Category");
        savedEntity.setParentCategory(parentEntity);

        when(categoryRepository.existsByName("Child Category")).thenReturn(false);
        when(categoryMapper.toEntity(inputDto)).thenReturn(childEntity);

        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parentEntity));

        when(categoryRepository.save(childEntity)).thenReturn(savedEntity);

        when(categoryMapper.toDTO(savedEntity)).thenReturn(new CategoryDTO(2L, "Child Category", parentId, "Parent Category", true));

        CategoryDTO resultDto = categoryService.createCategory(inputDto);

        assertNotNull(resultDto);
        assertEquals("Parent Category", resultDto.parentName());

        assertEquals(parentEntity, childEntity.getParentCategory());
    }

    @Test
    void create_ShouldThrowException_WhenParentNotFound() {

        Long nonExistentParentId = 99L;
        CategoryDTO inputDto = new CategoryDTO(null, "Child Category", nonExistentParentId, null, true);
        Category childEntity = new Category();

        when(categoryRepository.existsByName("Child Category")).thenReturn(false);
        when(categoryMapper.toEntity(inputDto)).thenReturn(childEntity);

        when(categoryRepository.findById(nonExistentParentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.createCategory(inputDto);
        });

        verify(categoryRepository, never()).save(any());
    }
}