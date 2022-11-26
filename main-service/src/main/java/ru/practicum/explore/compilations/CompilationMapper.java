package ru.practicum.explore.compilations;

import ru.practicum.explore.compilations.dto.CompilationDto;
import ru.practicum.explore.compilations.dto.NewCompilationDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.dto.EventShortDto;

import java.util.List;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto compilationDto, List<Event> events) {
        if (compilationDto == null) {
            return null;
        }

        return new Compilation(
                null,
                compilationDto.getTitle(),
                compilationDto.getPinned(),
                events
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtoList) {
        if (compilation == null) {
            return null;
        }

        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                eventShortDtoList
        );
    }
}
