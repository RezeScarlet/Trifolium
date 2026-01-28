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

        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Food", "food_icon", "#FFBBCC");

        Category category = new Category();
        category.setId(10L);
        category.setTitle("Food");
        category.setUser(user);

        CategoryResponseDTO responseDTO = new CategoryResponseDTO(10L, "Food", "food_icon", "#FFBBCC");

        when(categoryMapper.toEntity(requestDTO, user)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(responseDTO);

        CategoryResponseDTO resultado = categoryService.create(requestDTO, user);

        assertThat(resultado).isNotNull();
        assertThat(resultado.title()).isEqualTo("Food");

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

}
