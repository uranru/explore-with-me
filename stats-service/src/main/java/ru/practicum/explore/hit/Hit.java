package ru.practicum.explore.hit;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private Long id;
    @NotNull @NotEmpty
    @Column(name = "hit_app")
    private String app;
    @Column(name = "hit_uri")
    @NotNull @NotEmpty
    private String uri;
    @Column(name = "hit_ip")
    private String ip;
    @Column(name = "hit_time")
    private LocalDateTime timestamp;
}
