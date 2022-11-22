package ru.practicum.explore.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class NewCompilationDto {
    @NotNull
    private String title;
    private Boolean pinned;
    @NotNull
    private List<Long> events;
}
