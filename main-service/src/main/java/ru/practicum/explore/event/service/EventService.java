package ru.practicum.explore.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.enums.EventState;
import ru.practicum.explore.event.enums.EventStateResolution;
import ru.practicum.explore.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto addUserEvent(NewEventDto newEventDto, Long initiatorId);

    EventFullDto approvalAdminEvent(Long eventId);

    EventFullDto rejectAdminEvent(Long eventId, CommentEventDto commentEventDto);

    Event getEventById(Long eventId);

    EventFullDto getEventFullDtoById(Long eventId, HttpServletRequest request);

    EventFullDto getUserEventFullDtoById(Long userId, Long eventId);

    List<EventShortDto> getUserEventShortDtoById(Long initiatorId, Pageable pageable);

    EventFullDto updateUserEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto cancelUserEvent(Long userId, Long eventId);

    EventFullDto updateAdminEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                      String rangeStart, String rangeEnd, Pageable pageable);

    List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, Pageable pageable);

    List<Event> toEventList(List<Long> eventIdList);

    List<EventShortDto> toEventShortDtoList(List<Event> eventList);
}
