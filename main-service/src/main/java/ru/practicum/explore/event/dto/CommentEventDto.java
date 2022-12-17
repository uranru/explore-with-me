package ru.practicum.explore.event.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentEventDto {
    private Long id;

    @NotEmpty
    @NotNull
    private String text;
}
