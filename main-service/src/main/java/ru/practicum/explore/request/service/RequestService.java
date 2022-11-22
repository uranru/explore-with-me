package ru.practicum.explore.request.service;

import ru.practicum.explore.event.Event;
import ru.practicum.explore.request.ParticipationRequestDto;
import ru.practicum.explore.request.RequestStateResolution;

import java.util.List;
import java.util.Map;

public interface RequestService {
    ParticipationRequestDto addUserRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    List<ParticipationRequestDto> getUserRequestsByEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelOwnUserRequest(Long userId, Long reqId);

    ParticipationRequestDto approvalUserRequest(Long userId, Long eventId, Long reqId, RequestStateResolution resolution);

}
