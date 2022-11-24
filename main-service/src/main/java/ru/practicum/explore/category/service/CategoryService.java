package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto category);

    void deleteCategoryById(Long id);

    CategoryDto getCategoryDtoById(Long id);

    Category getCategoryById(Long id);

    CategoryDto updateCategory(CategoryDto category);

    List<CategoryDto> findAllCategoryOrderById(int from, int size);

}
