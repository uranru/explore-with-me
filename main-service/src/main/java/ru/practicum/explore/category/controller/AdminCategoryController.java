package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping(value = "/admin/categories")
    public CategoryDto addСategory(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto newСategoryDto = categoryService.addCategory(categoryDto);
        log.info("Выполнен запрос на добавление нового объекта: {}",newСategoryDto);

        return newСategoryDto;
    }

    @DeleteMapping("/admin/categories/{id}")
    public void deleteСategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        log.info("Выполнен запрос на удаление категории с ID {}", id);
    }

    @PatchMapping(value = "/admin/categories")
    public CategoryDto updateСategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto newСategoryDto = categoryService.updateCategory(categoryDto);
        log.info("Выполнен запрос на обновление категории: {}",newСategoryDto);

        return newСategoryDto;
    }

}
