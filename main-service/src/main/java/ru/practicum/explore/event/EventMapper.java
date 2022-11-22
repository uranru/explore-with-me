package ru.practicum.explore.event;


import ru.practicum.explore.category.Category;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.user.User;
import ru.practicum.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EventMapper {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        if (newEventDto == null) {
            return null;
        }

        return new Event(
                null,
                newEventDto.getAnnotation(),
                category,
                newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(),formatter),
                newEventDto.getLocation().get("lat"),
                newEventDto.getLocation().get("lon"),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                newEventDto.getRequestModeration(),
                newEventDto.getTitle(),
                LocalDateTime.now(),
                null,
                initiator,
                0,

                EventState.PENDING
        );
    }

    public static EventShortDto toEventShortDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto) {
        if (event == null) {
            return null;
        }


        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryDto,
                event.getDescription(),
                event.getEventDate().format(formatter),
                event.getPaid(),
                event.getTitle(),
                userShortDto,
                event.getConfirmedRequests(),
                null
        );

    }

    public static EventFullDto toEventFullDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto) {
        if (event == null) {
            return null;
        }

        String publishedOn = null;
        if (event.getPublishedOn() != null) {
            publishedOn = event.getPublishedOn().format(formatter);
        }

        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryDto,
                event.getDescription(),
                event.getEventDate().format(formatter),
                Map.of("lat",event.getLocationLat(), "lon", event.getLocationLon()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getTitle(),
                userShortDto,
                event.getConfirmedRequests(),
                event.getCreatedOn().format(formatter),
                publishedOn,
                null,
                event.getState()
        );
    }

}
