package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping(value = "/admin/categories")
    public CategoryDto addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto newCategoryDto = categoryService.addCategory(categoryDto);
        log.info("Выполнен запрос на добавление нового объекта: {}",newCategoryDto);

        return newCategoryDto;
    }

    @DeleteMapping("/admin/categories/{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        log.info("Выполнен запрос на удаление категории с ID {}", id);

        try {
            categoryService.deleteCategoryById(id);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        } catch (EmptyResultDataAccessException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }
    }

    @PatchMapping(value = "/admin/categories")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto newCategoryDto = categoryService.updateCategory(categoryDto);
        log.info("Выполнен запрос на обновление категории: {}",newCategoryDto);

        return newCategoryDto;
    }

}
