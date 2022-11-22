package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.Сategory;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto category);

    void deleteCategoryById(Long id);

    CategoryDto getCategoryDtoById(Long id);

    Сategory getCategoryById(Long id);

    CategoryDto updateCategory(CategoryDto category);

    public List<CategoryDto> findAllCategoryOrderById(int from, int size);

}
