package ru.practicum.explore.hit;

import ru.practicum.explore.hit.dto.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit toHit(EndpointHit endpointHit) {
        if (endpointHit == null) {
            return null;
        }

        return Hit.builder()
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(),formatter))
                .build();
    }
}
