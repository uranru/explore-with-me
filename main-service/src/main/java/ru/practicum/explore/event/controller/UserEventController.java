package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.event.dto.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserEventController {

    private final EventService eventService;

    @PostMapping(value = "/users/{userId}/events")
    public EventFullDto addUserEvent(
            @RequestBody @NotNull NewEventDto newEventDto,
            @PathVariable @Positive Long userId) {

        log.info("Выполнен запрос POST /users/{}/events: {}",userId, newEventDto);
        EventFullDto eventFullDto = eventService.addUserEvent(newEventDto, userId);

        return eventFullDto;
    }

    @GetMapping(value = "/users/{userId}/events")
    public List<EventShortDto> getUserEventById(
            @PathVariable @Positive Long userId,
            @RequestParam(defaultValue = "1") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("Выполнен запрос:: GET /users/{}/events", userId);
        List<EventShortDto> eventShortDtoList = eventService
                .getUserEventShortDtoById(userId, PageRequest.of(from / size,size));
        log.info("Получен ответ: {}", eventShortDtoList);

        return eventShortDtoList;
    }

    @GetMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {

        log.info("Выполнен запрос GET /users/{}/events/{}", userId, eventId);
        EventFullDto eventFullDto = eventService.getUserEventFullDtoById(userId, eventId);
        log.info("Получен ответ: {}", eventFullDto);

        return eventFullDto;
    }

    @PatchMapping(value = "/users/{userId}/events")
    public EventFullDto updateUserEvent(
            @PathVariable @Positive Long userId,
            @RequestBody @NotNull UpdateEventRequest updateEventRequest) {

        log.info("Выполнен запрос PATCH /users/{}/events: {}", userId, updateEventRequest);
        EventFullDto eventFullDto = eventService.updateUserEvent(userId, updateEventRequest);
        log.info("Получен ответ: {}", eventFullDto);

        return eventFullDto;
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}")
    public EventFullDto cancelUserEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {

        log.info("Выполнен запрос PATCH /users/{}/events/{}", userId, eventId);
        EventFullDto eventFullDto = eventService.cancelUserEvent(userId, eventId);
        log.info("Получен ответ: {}", eventFullDto);

        return eventFullDto;
    }

}

