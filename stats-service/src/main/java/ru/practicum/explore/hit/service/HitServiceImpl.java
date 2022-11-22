package ru.practicum.explore.hit.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.hit.Hit;
import ru.practicum.explore.hit.QHit;
import ru.practicum.explore.hit.dto.EndpointHit;
import ru.practicum.explore.hit.HitMapper;
import ru.practicum.explore.hit.HitRepository;
import ru.practicum.explore.hit.dto.ShortViewHit;
import ru.practicum.explore.hit.dto.ViewHit;
import ru.practicum.explore.hit.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService{

    final HitRepository hitRepository;

    @Override
    public void addHit(EndpointHit endpointHit) {
        log.debug("Выполнен метод:: addHit({})", endpointHit);

        hitRepository.save(HitMapper.toHit(endpointHit));
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        log.debug("Выполнен метод:: getStats(...)");

        QHit qHit = QHit.hit;
        BooleanExpression customerExpression = qHit.timestamp.after(LocalDateTime.parse(start,HitMapper.formatter))
                .and(qHit.timestamp.before(LocalDateTime.parse(end,HitMapper.formatter)));

        if (uris != null && uris.size() > 0) {
            customerExpression = customerExpression.and(qHit.uri.in(uris));
        }

        log.debug("Query DSL customerExpression:: {}", customerExpression);

        List<Hit> hitList = hitRepository.findAll(customerExpression, Pageable.unpaged()).getContent();

        if (unique == false) {
            return getViewStats(hitList);
        } else {
            return getViewStatsUnique(hitList);
        }

    }

    private List<ViewStats> getViewStatsUnique(List<Hit> hitList) {
        log.debug("Выполнен метод:: getViewStatsUnique(...)");

        Map<ViewHit, Integer> mapViewHits = new HashMap<>();
        Map<ShortViewHit, Integer> mapShortViewHits = new HashMap<>();
        List<ViewStats> viewStatsList = new ArrayList<>();
        for (Hit hit: hitList) {
            ViewHit viewHit = ViewHit.builder()
                    .app(hit.getApp())
                    .uri(hit.getUri())
                    .ip(hit.getIp())
                    .build();

            if (!mapViewHits.containsKey(viewHit)) {
                mapViewHits.put(viewHit,1);

                ShortViewHit shortViewHit = ShortViewHit.builder()
                        .app(hit.getApp())
                        .uri(hit.getUri())
                        .build();

                if (mapShortViewHits.containsKey(shortViewHit)) {
                    mapShortViewHits.put(shortViewHit,mapShortViewHits.get(shortViewHit) + 1);
                }
                else {
                    mapShortViewHits.put(shortViewHit,1);
                }
            }
        }

        mapShortViewHits.forEach((k,v) -> viewStatsList.add(ViewStats.builder()
                .app(k.getApp())
                .uri(k.getUri())
                .hits(v)
                .build()));

        return viewStatsList;
    }

    private List<ViewStats> getViewStats(List<Hit> hitList) {
        log.debug("Выполнен метод:: getViewStats(...)");

        Map<ShortViewHit, Integer> mapShortViewStats = new HashMap<>();
        List<ViewStats> viewStatsList = new ArrayList<>();
        for (Hit hit: hitList) {
            ShortViewHit shortViewStats = ShortViewHit.builder()
                    .app(hit.getApp())
                    .uri(hit.getUri())
                    .build();

            if (mapShortViewStats.containsKey(shortViewStats)) {
                mapShortViewStats.put(shortViewStats,mapShortViewStats.get(shortViewStats) + 1);
            }
            else {
                mapShortViewStats.put(shortViewStats,1);
            }
        }

        mapShortViewStats.forEach((k,v) -> viewStatsList.add(ViewStats.builder()
                .app(k.getApp())
                .uri(k.getUri())
                .hits(v)
                .build()));

        return viewStatsList;
    }

}
