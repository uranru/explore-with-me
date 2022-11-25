package ru.practicum.explore.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.request.service.RequestService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserRequestController {
    private final RequestService requestService;

    @PostMapping(value = "/users/{userId}/requests")
    public ParticipationRequestDto addRequest(
            @PathVariable @Positive Long userId,
            @RequestParam @Positive Long eventId) {

        log.info("Выполнен запрос POST /users/{}/requests?eventId={})", userId,eventId);
        ParticipationRequestDto participationRequestDto = requestService
                .addUserRequest(userId,eventId);
        log.info("Получен ответ: {}", participationRequestDto);

        return participationRequestDto;
    }

    @GetMapping(value = "/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(
            @PathVariable @Positive Long userId) {

        log.info("Выполнен запрос GET /users/{}/requests)", userId);
        List<ParticipationRequestDto> participationRequestDtoList = requestService
                .getUserRequests(userId);
        log.info("Получен ответ: {}", participationRequestDtoList);

        return participationRequestDtoList;
    }

    @GetMapping(value = "/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserRequestsByEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {

        log.info("Выполнен запрос GET /users/{}/events/{}/requests)", userId, eventId);
        List<ParticipationRequestDto> participationRequestDtoList = requestService
                .getUserRequestsByEvent(userId,eventId);
        log.info("Получен ответ: {}", participationRequestDtoList);

        return participationRequestDtoList;
    }

    @PatchMapping(value = "/users/{userId}/requests/{reqId}/cancel")
    public ParticipationRequestDto cancelOwnUserRequest(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long reqId) {

        log.info("Выполнен запрос PATCH /users/{}/requests/{}/cancel)", userId, reqId);
        ParticipationRequestDto participationRequestDto = requestService
                .cancelOwnUserRequest(userId,reqId);
        log.info("Получен ответ: {}", participationRequestDto);

        return participationRequestDto;
    }

    @PatchMapping(value = "/users/{userId}/events/{eventId}/requests/{reqId}/{resolution}")
    public ParticipationRequestDto approvalUserRequest(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @PathVariable @Positive Long reqId,
            @PathVariable @NotNull RequestStateResolution resolution) {

        log.info("Выполнен запрос PATCH /users/{}/events/{}/requests/{}/{})", userId, eventId, reqId, resolution);
        ParticipationRequestDto participationRequestDto = requestService
                .approvalUserRequest(userId,eventId,reqId,resolution);
        log.info("Получен ответ: {}", participationRequestDto);

        return participationRequestDto;
    }

}
