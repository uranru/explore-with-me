package ru.practicum.explore.category;

import ru.practicum.explore.category.dto.СategoryDto;

public class СategoryMapper {

    public static СategoryDto toСategoryDto(Сategory category) {
        СategoryDto categoryDto = new СategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setId(category.getId());

        return categoryDto;
    }

    public static Сategory toСategory(СategoryDto categoryDto) {
        Сategory category = new Сategory();
        category.setName(categoryDto.getName());
        category.setId(categoryDto.getId());

        return category;
    }

}
