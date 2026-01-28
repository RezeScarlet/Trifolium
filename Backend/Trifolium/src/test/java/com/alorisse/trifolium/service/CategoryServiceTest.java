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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        category.setColor(color);
        category.setIcon(icon);
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
        categoryByUser.setUser(user);

        Category categoryBySystem = new Category();
        categoryBySystem.setId(idBySystem);
        categoryBySystem.setTitle(titleBySystem);
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

        assertThat(result).containsExactlyInAnyOrder(responseByUser, responseBySystem);

        verify(categoryRepository, times(1)).findAllByUserIdOrSystemOrderById(user.getId());
    }

    @Test
    @DisplayName("Should update Category successfully")
    void shouldUpdateCategorySuccessfully() {
        User user = new User();
        user.setId(1L);

        Long id = 10L;

        String oldTitle = "Food";
        String oldColor = "#FFBBCC";
        String oldIcon = "food_icon";

        String newTitle = "Fare";
        String newColor = "#FFFFBB";
        String newIcon = "fare_icon";

        CategoryRequestDTO newRequest = new CategoryRequestDTO(newTitle, newColor, newIcon);

        Category oldCategory = new Category();
        oldCategory.setId(id);
        oldCategory.setTitle(oldTitle);
        oldCategory.setColor(oldColor);
        oldCategory.setIcon(oldIcon);
        oldCategory.setUser(user);

        Category newCategory = new Category();
        newCategory.setId(id);
        newCategory.setTitle(newTitle);
        newCategory.setColor(newColor);
        newCategory.setIcon(newIcon);
        newCategory.setUser(user);

        CategoryResponseDTO expectedResponse = new CategoryResponseDTO(id, newTitle, newColor, newIcon);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(oldCategory));

        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        when(categoryMapper.toDTO(newCategory)).thenReturn(expectedResponse);

        CategoryResponseDTO result = categoryService.update(id, newRequest, user);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.title()).isEqualTo(newTitle);
        assertThat(result.color()).isEqualTo(newColor);
        assertThat(result.icon()).isEqualTo(newIcon);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Should delete Category")
    void shouldDeleteCategory() {
        User user = new User();
        user.setId(1L);

        Long id = 10L;
        String title = "Food";

        Category category = new Category();
        category.setId(id);
        category.setTitle(title);
        category.setUser(user);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.delete(id, user);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    @DisplayName("Should throw exception when Category not Found")
    void shouldThrowExceptionWhenCategoryNotFound() {
        User user = new User();
        user.setId(1L);

        Long invalidID = 999L;

        String title = "Food";
        String color = "#FFBBCC";
        String icon = "food_icon";

        CategoryRequestDTO request = new CategoryRequestDTO(title, color, icon);

        when(categoryRepository.findById(invalidID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            categoryService.update(invalidID, request, user);
        });

        verify(categoryRepository, times(1)).findById(invalidID);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when User is not owner")
    void shouldThrowExceptionWhenUserIsNotOwner() {
        User owner = new User();
        owner.setId(1L);

        Long otherUserId = 999L;

        User otherUser = new User();
        otherUser.setId(otherUserId);

        Long id = 10L;
        String title = "Food";

        Category category = new Category();
        category.setId(id);
        category.setTitle(title);
        category.setUser(owner);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        assertThrows(RuntimeException.class, () -> {
            categoryService.delete(id, otherUser);
        });

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, never()).delete(any());
    }
}