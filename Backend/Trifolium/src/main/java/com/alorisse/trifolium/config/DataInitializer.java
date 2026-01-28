package com.alorisse.trifolium.config;

import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> defaultCategories = Arrays.asList(
                "CAT_BILLS",
                "CAT_DINING",
                "CAT_EDUCATION",
                "CAT_GROCERIES",
                "CAT_ENTERTAINMENT",
                "CAT_FREELANCE",
                "CAT_HEALTH",
                "CAT_SALARY",
                "CAT_SHOPPING",
                "CAT_TRANSPORT"
        );

        for (String title : defaultCategories) {
            if (!categoryRepository.existsByTitleAndUserIsNull(title)) {
                Category category = new Category();
                category.setTitle(title);
                category.setUser(null);
                category.setColor(null);
                category.setIcon(null);

                categoryRepository.save(category);
            }
        }
    }
}
