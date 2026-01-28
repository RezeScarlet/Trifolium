package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.CategoryMapper;
import com.alorisse.trifolium.model.dto.CategoryRequestDTO;
import com.alorisse.trifolium.model.dto.CategoryResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

        Long id = 10L;
        String title = "Food";
        String color = "#FFBBCC";
        String icon = "food_icon";

        CategoryRequestDTO request = new CategoryRequestDTO(title, color, icon);

        Category category = new Category();
        category.setId(id);
        category.setTitle(title);
        category.setUser(user);

        CategoryResponseDTO expectedResponse = new CategoryResponseDTO(id, title, color, icon);

        when(categoryMapper.toEntity(request, user)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(expectedResponse);

        CategoryResponseDTO result = categoryService.create(request, user);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.color()).isEqualTo(color);
        assertThat(result.icon()).isEqualTo(icon);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should list User and System Categories")
    void shouldListUserAndSystemCategories() {
        User user = new User();
        user.setId(1L);

        Long idByUser = 10L;
        String titleByUser = "Food";
        String colorByUser = "#FFBBCC";
        String iconByUser = "food_icon";

        Long idBySystem = 11L;
        String titleBySystem = "Fare";
        String colorBySystem = "#FFFFBB";
        String iconBySystem = "fare_icon";

        Category categoryByUser = new Category();
        categoryByUser.setId(idByUser);
        categoryByUser.setTitle(titleByUser);
        categoryByUser.setColor(colorByUser);
        categoryByUser.setIcon(iconByUser);
        categoryByUser.setUser(user);

        Category categoryBySystem = new Category();
        categoryBySystem.setId(idBySystem);
        categoryBySystem.setTitle(titleBySystem);
        categoryBySystem.setColor(colorBySystem);
        categoryBySystem.setIcon(iconBySystem);
        categoryBySystem.setUser(null);

        List<Category> entitiesList = List.of(categoryByUser, categoryBySystem);

        CategoryResponseDTO responseByUser = new CategoryResponseDTO(
                idByUser,
                titleByUser,
                colorByUser,
                iconByUser);

        CategoryResponseDTO responseBySystem = new CategoryResponseDTO(
                idBySystem,
                titleBySystem,
                colorBySystem,
                iconBySystem);

        when(categoryRepository.findAllByUserIdOrSystemOrderById(user.getId())).thenReturn(entitiesList);

        when(categoryMapper.toDTO(categoryByUser)).thenReturn(responseByUser);
        when(categoryMapper.toDTO(categoryBySystem)).thenReturn(responseBySystem);

        List<CategoryResponseDTO> result = categoryService.listAll(user);

        assertThat(result).containsExactlyInAnyOrder(responseByUser);
        assertThat(result).containsExactlyInAnyOrder(responseBySystem);

        verify(categoryRepository, times(1)).findAllByUserIdOrSystemOrderById(user.getId());
    }
}