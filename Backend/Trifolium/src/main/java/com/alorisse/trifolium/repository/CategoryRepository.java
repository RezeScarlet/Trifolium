package com.alorisse.trifolium.repository;

import com.alorisse.trifolium.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.user.id = :userId OR c.user IS NULL")
    List<Category> findAllByUserIdOrSystemOrderById(Long userId);

    boolean existsByTitleAndUserIsNull(String title);
}
