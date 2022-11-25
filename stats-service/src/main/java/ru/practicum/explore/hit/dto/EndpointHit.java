package ru.practicum.explore.hit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class EndpointHit {
    private Long id;
    @NotNull @NotEmpty
    private String app;
    @NotNull @NotEmpty
    private String uri;
    @NotNull @NotEmpty
    private String ip;
    @NotNull @NotEmpty
    private String timestamp;
}
