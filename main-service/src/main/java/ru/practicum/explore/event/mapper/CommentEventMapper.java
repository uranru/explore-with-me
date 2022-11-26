package ru.practicum.explore.event.mapper;

import ru.practicum.explore.event.dto.CommentEventDto;
import ru.practicum.explore.event.model.CommentEvent;
import ru.practicum.explore.event.model.Event;

public class CommentEventMapper {

    public static CommentEvent toCommentEvent(CommentEventDto commentEventDto, Event event) {
        if (commentEventDto == null) {
            return null;
        }

        return CommentEvent.builder()
                .text(commentEventDto.getText())
                .event(event)
                .build();
    }

    public static CommentEventDto toCommentEventDto(CommentEvent commentEvent) {
        if (commentEvent == null) {
            return null;
        }

        return CommentEventDto.builder()
                .id(commentEvent.getId())
                .text(commentEvent.getText())
                .build();
    }

}
