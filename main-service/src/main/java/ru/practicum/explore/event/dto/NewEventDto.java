package ru.practicum.explore.event.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@ToString
@Getter
@Setter
public class NewEventDto {
    String annotation;
    Long category;
    String description;
    String eventDate;
    Map<String,Integer> location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String title;
}
