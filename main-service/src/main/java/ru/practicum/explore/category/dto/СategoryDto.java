package ru.practicum.explore.category.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class СategoryDto {
    Long id;
    @NotNull @NotEmpty
    String name;
    public СategoryDto() {
    }
}

