package ru.practicum.explore.request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        if (request == null) {
            return null;
        }

        return new ParticipationRequestDto(
                request.getId(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getCreated().format(formatter),
                request.getStatus()
        );

    }

}
