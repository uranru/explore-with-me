package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.СategoryDto;
import ru.practicum.explore.category.service.СategoryService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminСategoryController {

    private final СategoryService categoryService;

    @PostMapping(value = "/admin/categories")
    public СategoryDto addСategory(@RequestBody @Valid СategoryDto categoryDto) {
        СategoryDto newСategoryDto = categoryService.addСategory(categoryDto);
        log.info("Выполнен запрос на добавление нового объекта: {}",newСategoryDto);

        return newСategoryDto;
    }

    @DeleteMapping("/admin/categories/{id}")
    public void deleteСategoryById(@PathVariable Long id) {
        categoryService.deleteСategoryById(id);
        log.info("Выполнен запрос на удаление категории с ID {}", id);
    }

    @PatchMapping(value = "/admin/categories")
    public СategoryDto updateСategory(@RequestBody СategoryDto categoryDto) {
        СategoryDto newСategoryDto = categoryService.updateСategory(categoryDto);
        log.info("Выполнен запрос на обновление категории: {}",newСategoryDto);

        return newСategoryDto;
    }

}
