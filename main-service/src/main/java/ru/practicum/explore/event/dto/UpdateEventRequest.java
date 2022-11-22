package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class UpdateEventRequest {
    Long eventId;
    String annotation;
    Long category;
    String description;
    String eventDate;
    Boolean paid;
    Integer participantLimit;
    String title;
}
