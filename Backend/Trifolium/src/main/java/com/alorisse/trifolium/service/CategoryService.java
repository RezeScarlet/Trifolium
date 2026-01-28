package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.CategoryMapper;
import com.alorisse.trifolium.model.dto.CategoryRequestDTO;
import com.alorisse.trifolium.model.dto.CategoryResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto, User user) {
        Category category = categoryMapper.toEntity(dto, user);
        Category saved = categoryRepository.save(category);

        return categoryMapper.toDTO(saved);
    }

    public List<CategoryResponseDTO> listAll(User user) {
        return categoryRepository.findAllByUserIdOrSystemOrderById(user.getId()).stream().map(categoryMapper::toDTO).toList();
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto, User user) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found."));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User doesn't own this transaction.");
        }


        category.setTitle(dto.title());
        category.setColor(dto.color());
        category.setIcon(dto.icon());

        Category updated = categoryRepository.save(category);
        return categoryMapper.toDTO(updated);
    }

    public void delete(Long id, User user) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found."));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User doesn't own this transaction.");
        }

        categoryRepository.delete(category);
    }

}
