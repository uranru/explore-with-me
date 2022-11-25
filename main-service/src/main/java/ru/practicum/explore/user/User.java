package ru.practicum.explore.user;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long id;
    @Column(name = "user_name")
    @NotEmpty
    @NotNull
    String name;
    @Email
    @NotNull
    @Column(name = "user_email")
    String email;

    public User() {
    }

}