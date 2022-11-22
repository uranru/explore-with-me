package ru.practicum.explore.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.explore.event.Event;
import ru.practicum.explore.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "request_created")
    private LocalDateTime created;
    @Column(name = "request_state")
    private RequestState status;

    public Request() {
    }
}
