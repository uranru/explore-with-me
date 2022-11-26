package ru.practicum.explore.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.user.User;

import java.util.List;


public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findAllByRequesterOrderById(User requester);

    List<Request> findAllByEventOrderById(Event event);
}
