package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.СategoryDto;
import ru.practicum.explore.category.Сategory;

import java.util.List;

public interface СategoryService {

    СategoryDto addСategory(СategoryDto category);

    void deleteСategoryById(Long id);

    СategoryDto getСategoryDtoById(Long id);

    Сategory getСategoryById(Long id);

    СategoryDto updateСategory(СategoryDto category);

    public List<СategoryDto> findAllСategoryOrderById(int from, int size);

}
