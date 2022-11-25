package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.event.EventState;
import ru.practicum.explore.event.EventStateResolution;
import ru.practicum.explore.event.dto.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminEventController {

    private final EventService eventService;

    @PatchMapping(value = "/admin/events/{eventId}/{resolution}")
    public EventFullDto approvalAdminEvent(
            @PathVariable @Positive Long eventId,
            @PathVariable @NotNull EventStateResolution resolution) {

        log.info("Выполнен запрос PATCH /admin/events/{}/{}",eventId, resolution);
        EventFullDto eventFullDto = eventService.approvalAdminEvent(eventId, resolution);

        return eventFullDto;
    }

    @PutMapping(value = "/admin/events/{eventId}")
    public EventFullDto updateAdminEvent(
            @PathVariable @Positive Long eventId,
            @RequestBody @NotNull AdminUpdateEventRequest adminUpdateEventRequest) {

        log.info("Выполнен запрос PUT /admin/events/{}: {}",eventId, adminUpdateEventRequest);
        EventFullDto eventFullDto = eventService.updateAdminEvent(eventId, adminUpdateEventRequest);
        log.info("Получен ответ: {}", eventFullDto);

        return eventFullDto;
    }

    @GetMapping(value = "/admin/events")
    public List<EventFullDto> getAdminEvent(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("Выполнен запрос GET /admin/events");
        List<EventFullDto> eventFullDtoList = eventService
                .getAdminEvents(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size,size));
        log.debug("Получен ответ: {}", eventFullDtoList);

        return eventFullDtoList;
    }

}

