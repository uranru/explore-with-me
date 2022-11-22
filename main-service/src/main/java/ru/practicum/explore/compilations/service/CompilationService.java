package ru.practicum.explore.compilations.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto adminAddCompilation(NewCompilationDto newCompilationDto);

    CompilationDto getCompilationById(Long id);

    CompilationDto adminUpdateCompilationAddEvent(Long compilationId,Long eventId);

    CompilationDto adminUpdateCompilationDeleteEvent(Long compilationId,Long eventId);

    CompilationDto adminUpdateCompilationPin(Long compilationId, Boolean pinned);

    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    void adminDeleteCompilation(Long compilationId);
}
