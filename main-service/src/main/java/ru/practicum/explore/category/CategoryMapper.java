package ru.practicum.explore.category;

import ru.practicum.explore.category.dto.CategoryDto;

public class CategoryMapper {

    public static CategoryDto toСategoryDto(Сategory category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setId(category.getId());

        return categoryDto;
    }

    public static Сategory toСategory(CategoryDto categoryDto) {
        Сategory category = new Сategory();
        category.setName(categoryDto.getName());
        category.setId(categoryDto.getId());

        return category;
    }

}
