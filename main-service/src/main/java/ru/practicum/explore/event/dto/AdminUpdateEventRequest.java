package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class AdminUpdateEventRequest {
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
