package ru.practicum.explore.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminUserController {

    private final UserService userService;

    @PostMapping(value = "/admin/users")
    public UserDto addUser(@RequestBody UserDto userDto) {
        UserDto newUserDto = userService.addUser(userDto);
        log.info("Выполнен запрос на добавление нового объекта: {}",newUserDto);

        return newUserDto;
    }

    @GetMapping("/admin/users")
    public List<UserDto> getUserById(@RequestParam(defaultValue = "0") Long ids) {
        List<UserDto> userDtoList = userService.getUsers(ids);

        log.info("Выполнен запрос на получение пользователей {}", userDtoList);

        return userDtoList;
    }

    @DeleteMapping("/admin/users/{id}")
    public void deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "");
        }

        log.info("Выполнен запрос на удаление пользователя с ID {}", id);
    }

}
