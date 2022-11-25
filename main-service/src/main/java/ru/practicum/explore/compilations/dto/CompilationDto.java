package ru.practicum.explore.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explore.event.dto.EventShortDto;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;
}
