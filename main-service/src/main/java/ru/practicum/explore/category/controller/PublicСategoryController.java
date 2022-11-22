package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.СategoryDto;
import ru.practicum.explore.category.service.СategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PublicСategoryController {

    private final СategoryService categoryService;

    @GetMapping("/categories/{id}")
    public СategoryDto getСategoryById(@PathVariable Long id,
            HttpServletRequest request) {

        log.info("Выполнен запрос:: client IP {}, endpoint path {}", request.getRemoteAddr(), request.getRequestURI());
        СategoryDto categoryDto = categoryService.getСategoryDtoById(id);

        return categoryDto;
    }

    @GetMapping("/categories")
    public List<СategoryDto> findAllСategory(@RequestParam(defaultValue = "1") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Выполнен запрос на получение всех категорий");

        return categoryService.findAllСategoryOrderById(from, size);
    }

}
