package ru.practicum.explore.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.hit.dto.EndpointHit;
import ru.practicum.explore.hit.dto.ViewStats;
import ru.practicum.explore.hit.service.HitService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HitController {

    private final HitService hitService;

    @PostMapping(value = "/hit")
    public void addHit(
            @RequestBody @NotNull EndpointHit endpointHit,
            HttpServletRequest request) {

        log.info("Выполнен запрос:: POST {}", request.getRequestURI());
        hitService.addHit(endpointHit);
    }

    @GetMapping(value = "/stats")
    public List<ViewStats> getStats(
            @RequestParam @NotEmpty String start,
            @RequestParam @NotEmpty String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique,
            HttpServletRequest request) {

        log.info("Выполнен запрос:: GET {}", request.getRequestURI());
        List<ViewStats> viewStats = hitService
                .getStats(start,end,uris,unique);
        log.debug("Получен ответ:: {}", viewStats);

        return viewStats;
    }
}
