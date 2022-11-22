package ru.practicum.explore.hit.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ShortViewHit {
    @NotNull
    @NotEmpty
    private String app;
    @NotNull @NotEmpty
    private String uri;
}
