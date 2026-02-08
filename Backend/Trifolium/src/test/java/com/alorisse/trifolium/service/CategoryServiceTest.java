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
import org.mockito.*;
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

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @Test
    @DisplayName("Should create a category successfully")
    void shouldCreateCategorySuccessfully() {
        User user = buildUser();

        Category category = buildCategory(user);
        Long id = category.getId();
        String title = category.getTitle();
        String color = category.getColor();
        String icon = category.getIcon();

        CategoryRequestDTO request = new CategoryRequestDTO(title, color, icon);


        CategoryResponseDTO response = new CategoryResponseDTO(id, title, color, icon);

        when(categoryMapper.toEntity(request, user)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(response);

        CategoryResponseDTO result = categoryService.create(request, user);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.color()).isEqualTo(color);
        assertThat(result.icon()).isEqualTo(icon);

        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Should return all user and system categories successfully")
    void shouldReturnAllUserAndSystemCategories() {
        User user = buildUser();

        Category categoryByUser = buildCategory(user);
        String titleByUser = categoryByUser.getTitle();
        String colorByUser = categoryByUser.getColor();
        String iconByUser = categoryByUser.getIcon();

        Long idBySystem = 11L;
        String titleBySystem = "Fare";
        String colorBySystem = "#FFFFBB";
        String iconBySystem = "fare_icon";

        Long idByUser = categoryByUser.getId();

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

        verify(categoryRepository).findAllByUserIdOrSystemOrderById(user.getId());
    }

    @Test
    @DisplayName("Should update category successfully")
    void shouldUpdateCategorySuccessfully() {
        User user = buildUser();

        Category oldCategory = buildCategory(user);
        Long id = oldCategory.getId();

        String newTitle = "Fare";
        String newColor = oldCategory.getColor();
        String newIcon = oldCategory.getIcon();

        CategoryRequestDTO newRequest = new CategoryRequestDTO(newTitle, newColor, newIcon);

        CategoryResponseDTO response = new CategoryResponseDTO(id, newTitle, newColor, newIcon);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(oldCategory));

        when(categoryRepository.save(any(Category.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        when(categoryMapper.toDTO(any(Category.class))).thenReturn(response);

        CategoryResponseDTO result = categoryService.update(id, newRequest, user);

        assertThat(result).isNotNull();

        verify(categoryRepository).save(categoryCaptor.capture());

        Category newCategory = categoryCaptor.getValue();

        assertThat(newCategory.getId()).isEqualTo(id);
        assertThat(newCategory.getTitle()).isEqualTo(newTitle);
        assertThat(newCategory.getColor()).isEqualTo(newColor);
        assertThat(newCategory.getIcon()).isEqualTo(newIcon);
    }

    @Test
    @DisplayName("Should delete category successfully")
    void shouldDeleteCategory() {
        User user = buildUser();

        Category category = buildCategory(user);
        Long id = category.getId();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.delete(id, user);

        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Should fail to update non-existent category")
    void shouldFailToUpdateNonExistentCategory() {
        User user = buildUser();

        Long invalidID = 999L;

        String title = "Food";
        String color = "#FFBBCC";
        String icon = "food_icon";

        CategoryRequestDTO request = new CategoryRequestDTO(title, color, icon);

        when(categoryRepository.findById(invalidID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.update(invalidID, request, user));

        verify(categoryRepository).findById(invalidID);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should fail to delete category belonging to another user")
    void shouldFailToDeleteCategoryOfAnotherUser() {
        User owner = buildUser();

        User notOwner = new User();
        notOwner.setId(999L);

        Category category = buildCategory(owner);

        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        assertThrows(RuntimeException.class, () -> categoryService.delete(id, notOwner));

        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).delete(category);
    }

    @Test
    @DisplayName("Should fail to update category belonging to another user")
    void shouldFailToUpdateCategoryOfAnotherUser() {
        User owner = buildUser();

        User anotherUser = new User();
        anotherUser.setId(999L);

        Category category = buildCategory(owner);
        Long id = category.getId();

        CategoryRequestDTO request = new CategoryRequestDTO("Other Title", null, null);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        assertThrows(RuntimeException.class, () -> categoryService.update(id, request, anotherUser));

        verify(categoryRepository).findById(id);
        verify(categoryRepository, never()).save(any());
    }

    private User buildUser() {
        User user = new User();
        user.setId(1L);

        return user;
    }

    private Category buildCategory(User user) {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Title");
        category.setColor("#FFBBCC");
        category.setIcon("food_icon");
        category.setUser(user);

        return category;
    }
}