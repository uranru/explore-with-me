package ru.practicum.explore.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore.event.EventMapper;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ViewClient extends BaseClient {

    public ViewClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/stats"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public Integer getHitsByEventId(String app, Long eventId) {
        String start = LocalDateTime.now().minusYears(1).format(EventMapper.formatter);
        String end = LocalDateTime.now().plusYears(1).format(EventMapper.formatter);

        ResponseEntity<Object> response =  get("?start=" + start + "&end=" + end + "&uris=/events/" + eventId + "&unique=true");

        Gson gson = new Gson();
        try {
            String object = gson.toJson(((List<Object>) response.getBody()).get(0));
            return gson.fromJson(object, ViewViews.class).getHits();
        } catch (IndexOutOfBoundsException exception) {
            return 0;
        }
    }
}
