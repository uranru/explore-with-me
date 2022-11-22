package ru.practicum.explore.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.service.CompilationService;
import ru.practicum.explore.event.EventSort;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping("/compilations/{compilationId}")
    public CompilationDto getCompilationById(
            @PathVariable @Positive Long compilationId) {

        log.info("Выполнен запрос GET /compilations/{}", compilationId);
        CompilationDto compilationDto = compilationService.getCompilationById(compilationId);
        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false, defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("Выполнен запрос GET /compilations");
        List<CompilationDto> compilationDtoList = compilationService.getCompilations(pinned, PageRequest.of(from / size,size));
        log.info("Получен ответ: {}", compilationDtoList);

        return compilationDtoList;
    }

}
