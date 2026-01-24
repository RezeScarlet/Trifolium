package com.alorisse.trifolium.mapper;

import com.alorisse.trifolium.model.dto.CategoryRequestDTO;
import com.alorisse.trifolium.model.dto.CategoryResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryRequestDTO dto, User user) {
        Category entity = new Category();
        entity.setTitle(dto.title());
        entity.setColor(dto.color());
        entity.setIcon(dto.icon());
        entity.setUser(user);
        return entity;

    }

    public CategoryResponseDTO toDTO (Category entity) {
        return new CategoryResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getColor(),
                entity.getIcon()
        );
    }
}
