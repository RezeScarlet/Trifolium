package com.alorisse.trifolium.controller;

import com.alorisse.trifolium.model.dto.CategoryRequestDTO;
import com.alorisse.trifolium.model.dto.CategoryResponseDTO;
import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.UserRepository;
import com.alorisse.trifolium.service.CategoryService;
import jakarta.validation.Valid;
import org.hibernate.validator.internal.util.actions.GetDeclaredMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !((authentication.getPrincipal()) instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return (User) authentication.getPrincipal();
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryRequestDTO dto, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        CategoryResponseDTO response = categoryService.create(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listAll(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(categoryService.listAll(user));

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CategoryRequestDTO dto, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(categoryService.update(id, dto, user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        categoryService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

}


