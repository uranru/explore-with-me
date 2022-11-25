package ru.practicum.explore.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserShortDto {
    Long id;
    String name;

    public UserShortDto() {
    }
}