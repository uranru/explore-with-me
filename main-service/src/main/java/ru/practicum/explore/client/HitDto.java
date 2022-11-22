package ru.practicum.explore.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@Builder
public class HitDto {
    @NotNull @NotEmpty
    private String app;
    @NotNull @NotEmpty
    private String uri;
    @NotNull @NotEmpty
    private String ip;
    @NotNull @NotEmpty
    private String timestamp;
}
