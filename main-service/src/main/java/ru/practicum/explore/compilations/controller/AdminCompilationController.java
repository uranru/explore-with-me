package ru.practicum.explore.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.dto.NewCompilationDto;
import ru.practicum.explore.compilations.service.CompilationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping(value = "/admin/compilations")
    public CompilationDto adminAddCompilation(
            @RequestBody @NotNull NewCompilationDto newCompilationDto) {

        log.info("Выполнен запрос POST /admin/compilations: {})", newCompilationDto);
        CompilationDto compilationDto = compilationService
                .adminAddCompilation(newCompilationDto);
        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @PatchMapping("/admin/compilations/{compilationId}/events/{eventId}")
    public CompilationDto adminUpdateCompilationAddEvent(
            @PathVariable @Positive Long compilationId,
            @PathVariable @Positive Long eventId) {

        log.info("Выполнен запрос PATCH /admin/compilations/{}/events/{}", compilationId, eventId);
        CompilationDto compilationDto = compilationService
                .adminUpdateCompilationAddEvent(compilationId,eventId);
        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @DeleteMapping("/admin/compilations/{compilationId}/events/{eventId}")
    public CompilationDto adminUpdateCompilationDeleteEvent(
            @PathVariable @Positive Long compilationId,
            @PathVariable @Positive Long eventId) {

        log.info("Выполнен запрос DEL /admin/compilations/{}/events/{}", compilationId, eventId);
        CompilationDto compilationDto = compilationService
                .adminUpdateCompilationDeleteEvent(compilationId,eventId);
        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @PatchMapping("/admin/compilations/{compilationId}/pin")
    public CompilationDto adminUpdateCompilationPinTrue(
            @PathVariable @Positive Long compilationId) {

        log.info("Выполнен запрос PATCH /admin/compilations/{compilationId}/pin", compilationId);
        CompilationDto compilationDto = compilationService
                .adminUpdateCompilationPin(compilationId, true);
        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @DeleteMapping("/admin/compilations/{compilationId}/pin")
    public CompilationDto adminUpdateCompilationPinFalse(
            @PathVariable @Positive Long compilationId) {

        log.info("Выполнен запрос DEL /admin/compilations/{compilationId}/pin", compilationId);
        CompilationDto compilationDto = compilationService
                .adminUpdateCompilationPin(compilationId, false);
        log.info("Получен ответ: {}", compilationDto);

        return compilationDto;
    }

    @DeleteMapping("/admin/compilations/{compilationId}")
    public void adminDeleteCompilation(
            @PathVariable @Positive Long compilationId) {

        log.info("Выполнен запрос DEL /admin/compilations/{}", compilationId);
        compilationService
                .adminDeleteCompilation(compilationId);
    }

}
