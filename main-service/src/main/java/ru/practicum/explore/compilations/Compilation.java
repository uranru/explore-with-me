package ru.practicum.explore.compilations;

import lombok.*;
import ru.practicum.explore.event.model.Event;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;
    @NotNull @NotEmpty
    @Column(name = "compilation_title")
    private String title;
    @Column(name = "compilation_pinned")
    @NotNull
    private Boolean pinned;
    @ManyToMany()
    @JoinTable(
            name = "compilations_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    private List<Event> events;
}
