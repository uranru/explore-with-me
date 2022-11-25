package ru.practicum.explore.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class UserDto {
    Long id;
    @NotEmpty @NotNull
    String name;
    @Email
    String email;

    public UserDto() {
    }
}