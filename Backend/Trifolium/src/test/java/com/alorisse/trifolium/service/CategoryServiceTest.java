package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.CategoryMapper;
import com.alorisse.trifolium.model.dto.CategoryRequestDTO;
import com.alorisse.trifolium.model.dto.CategoryResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.alorisse.trifolium.model.entity.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("Should create a category successfully")
    void shouldCreateCategorySuccessfully() {
        User user = new User();
        user.setId(1L);

        Long categoryId = 10L;
        String categoryTitle = "Food";
        String categoryColor = "#FFBBCC";
        String categoryIcon = "food_icon";

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO(
                categoryTitle,
                categoryColor,
                categoryIcon);

        Category categoryEntity = new Category();
        categoryEntity.setId(categoryId);
        categoryEntity.setTitle(categoryTitle);
        categoryEntity.setUser(user);

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(
                categoryId,
                categoryTitle,
                categoryColor,
                categoryIcon);

        when(categoryMapper.toEntity(categoryRequestDTO, user)).thenReturn(categoryEntity);
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryEntity);
        when(categoryMapper.toDTO(categoryEntity)).thenReturn(categoryResponseDTO);

        CategoryResponseDTO categoryResult = categoryService.create(categoryRequestDTO, user);

        assertThat(categoryResult).isNotNull();
        assertThat(categoryResult.title()).isEqualTo(categoryTitle);
        assertThat(categoryResult.id()).isEqualTo(categoryId);
        assertThat(categoryResult.color()).isEqualTo(categoryColor);
        assertThat(categoryResult.icon()).isEqualTo(categoryIcon);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
