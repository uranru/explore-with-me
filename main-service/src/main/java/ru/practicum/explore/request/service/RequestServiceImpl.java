package ru.practicum.explore.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.event.Event;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.request.*;
import ru.practicum.explore.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public ParticipationRequestDto addUserRequest(Long userId, Long eventId) {
        log.debug("Выполнен метод addRequest({},{})",userId,eventId);

        Request request = new Request(
                null,
                eventService.getEventById(eventId),
                userService.getUserById(userId),
                LocalDateTime.now(),
                RequestState.PENDING
        );

        Request newRequest = requestRepository.save(request);
        log.debug("Добавлен новый Request {}", newRequest);

        return RequestMapper.toParticipationRequestDto(newRequest);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.debug("Выполнен метод getUserRequests({})",userId);

        List<Request> requestList;
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();

        try {
            requestList = requestRepository.findAllByRequesterOrderById(userService.getUserById(userId));
        }  catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }

        requestList.stream().forEach(
                request -> participationRequestDtoList.add(
                        RequestMapper.toParticipationRequestDto(request
                        )
                )
        );

        return participationRequestDtoList;
    }

    @Override
    public List<ParticipationRequestDto> getUserRequestsByEvent(Long userId, Long eventId) {
        log.debug("Выполнен метод getUserRequestsByEvent({},{})",userId,eventId);

        List<Request> requestList;
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();
        Event event;

        try {
            event = eventService.getEventById(eventId);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }

        if (event.getInitiator().getId().equals(userId)) {
            try {
                requestList = requestRepository.findAllByEventOrderById(event);
            } catch (NoSuchElementException exception) {
                throw new ResponseStatusException(HttpStatus.resolve(404), "");
            }

            requestList.stream().forEach(
                    request -> participationRequestDtoList.add(
                            RequestMapper.toParticipationRequestDto(request
                            )
                    )
            );

            return participationRequestDtoList;

        } else {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }


    }

    @Override
    public ParticipationRequestDto cancelOwnUserRequest(Long userId, Long reqId) {
        log.debug("Выполнен метод cancelUserRequest({},{})", userId, reqId);
        Request request;

        try {
            request = requestRepository.findById(reqId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }

        if (!userId.equals(request.getRequester().getId())) {
            throw new ResponseStatusException(HttpStatus.resolve(403), "");
        }

        request.setStatus(RequestState.CANCELED);
        request = requestRepository.save(request);

        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto approvalUserRequest(Long userId, Long eventId, Long reqId, RequestStateResolution resolution) {
        log.debug("Выполнен метод approvalUserRequest({},{},{},{})", userId, eventId, reqId, resolution);

        Event event;
        Request request;
        RequestState requestState = RequestState.PENDING;

        try {
            event = eventService.getEventById(eventId);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        try {
            request = requestRepository.findById(reqId).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        switch (resolution) {
            case reject:
                request.setStatus(RequestState.REJECTED);
                break;
            case confirm:
                request.setStatus(RequestState.CONFIRMED);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
        }

        request = requestRepository.save(request);

        return RequestMapper.toParticipationRequestDto(request);
    }




}
