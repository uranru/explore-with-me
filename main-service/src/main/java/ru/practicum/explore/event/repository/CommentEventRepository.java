package ru.practicum.explore.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.event.model.CommentEvent;

public interface CommentEventRepository extends JpaRepository<CommentEvent,Long> {

    CommentEvent findCommentEventsByEvent_Id(Long eventId);
}
