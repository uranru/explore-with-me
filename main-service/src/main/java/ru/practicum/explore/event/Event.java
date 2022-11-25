package ru.practicum.explore.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import ru.practicum.explore.category.Category;
import ru.practicum.explore.user.User;

@ToString
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column(name = "event_annotation")
    @NotEmpty @NotNull
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "event_description")
    @NotEmpty @NotNull
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "event_location_lat")
    private Integer locationLat;
    @Column(name = "event_location_lon")
    private Integer locationLon;
    @Column(name = "event_paid")
    private Boolean paid;
    @Column(name = "event_participant_limit")
    private Integer participantLimit;
    @Column(name = "event_request_moderation")
    private Boolean requestModeration;
    @Column(name = "event_title")
    @NotEmpty @NotNull
    private String title;
    @Column(name = "event_created_on")
    private LocalDateTime createdOn;
    @Column(name = "event_published_on")
    private LocalDateTime publishedOn;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Column(name = "event_confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "event_state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    public Event() {
    }
}
