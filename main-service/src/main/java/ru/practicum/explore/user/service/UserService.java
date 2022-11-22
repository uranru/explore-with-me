package ru.practicum.explore.user.service;

import ru.practicum.explore.user.User;
import ru.practicum.explore.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    List<UserDto> getUsers(Long id);

    User getUserById(Long id);

    void deleteUserById(Long id);
}
