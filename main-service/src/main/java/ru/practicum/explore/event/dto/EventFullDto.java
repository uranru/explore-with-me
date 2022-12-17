package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.event.enums.EventState;
import ru.practicum.explore.user.dto.UserShortDto;

import java.util.Map;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    String description;
    String eventDate;
    Map<String,Integer> location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String title;
    UserShortDto initiator;
    Integer confirmedRequests;
    String createdOn;
    String publishedOn;
    Integer views;
    EventState state;
    CommentEventDto comment;
}
