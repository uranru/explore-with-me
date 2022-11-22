package ru.practicum.explore.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private String created;
    private RequestState status;
}
