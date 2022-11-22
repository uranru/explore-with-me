package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.event.EventSort;
import ru.practicum.explore.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PublicEventController {

    private final EventService eventService;

    @GetMapping(value = "/events/{eventId}")
    public EventFullDto getEventById(
            @PathVariable @Positive Long eventId,
            HttpServletRequest request) {

        log.info("Выполнен запрос GET {}", request.getRequestURI());
        EventFullDto eventFullDto = eventService.getEventFullDtoById(eventId, request);
        log.info("Получен ответ: {}", eventFullDto);

        return eventFullDto;
    }

    @GetMapping(value = "/events")
    public List<EventShortDto> getEvent(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") EventSort sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("Выполнен запрос GET /events");
        Sort sortEvent = null;
        switch (sort) {
            case EVENT_DATE:
                sortEvent = Sort.by("eventDate");
                break;
            case VIEWS:
                sortEvent = Sort.by("views");
        }
        List<EventShortDto> eventFullDtoList = eventService
                .getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, PageRequest.of(from / size,size,sortEvent));
        log.debug("Получен ответ: {}", eventFullDtoList);

        return eventFullDtoList;
    }

}

