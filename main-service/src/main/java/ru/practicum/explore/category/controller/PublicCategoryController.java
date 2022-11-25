package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id,
                                       HttpServletRequest request) {

        log.debug("Выполнен запрос:: client IP {}, endpoint path {}", request.getRemoteAddr(), request.getRequestURI());

        return categoryService.getCategoryDtoById(id);
    }

    @GetMapping("/categories")
    public List<CategoryDto> findAllСategory(@RequestParam(defaultValue = "1") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Выполнен запрос на получение всех категорий");

        return categoryService.findAllCategoryOrderById(from, size);
    }

}
