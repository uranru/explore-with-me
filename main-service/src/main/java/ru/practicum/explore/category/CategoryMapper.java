package ru.practicum.explore.category;

import ru.practicum.explore.category.dto.CategoryDto;

public class CategoryMapper {

    public static CategoryDto toСategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setId(category.getId());

        return categoryDto;
    }

    public static Category toСategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setId(categoryDto.getId());

        return category;
    }

}
