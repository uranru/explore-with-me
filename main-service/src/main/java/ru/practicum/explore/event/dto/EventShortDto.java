package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.user.dto.UserShortDto;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDto category;
    String description;
    String eventDate;
    Boolean paid;
    String title;
    UserShortDto initiator;
    Integer confirmedRequests;
    Integer views;
}
