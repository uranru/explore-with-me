package ru.practicum.explore.hit.service;

import ru.practicum.explore.hit.dto.EndpointHit;
import ru.practicum.explore.hit.dto.ViewStats;

import java.util.List;

public interface HitService {

    void addHit(EndpointHit endpointHit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
