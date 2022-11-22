package ru.practicum.explore.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.user.User;
import ru.practicum.explore.user.UserMapper;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.exeption.*;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto userDto) {
        log.debug("Выполнен метод addUser({})", userDto);
        User user = UserMapper.toUser(userDto);

        try {
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException exception) {
            throw new ApiStorageException(exception.getMessage());
        } catch (ConstraintViolationException exception) {
            throw new ApiConstraintViolationException(exception.getMessage());
        }
    }

    @Override
    public List<UserDto> getUsers(Long id) {
        log.debug("Выполнен метод getUsers({})", id);
        List<User> userList = new ArrayList<>();
        List<UserDto> userDtoList = new ArrayList<>();

        if (id >0) {
            try {
                User user = userRepository.findById(id).get();
                userList.add(user);
            } catch (NoSuchElementException exception) {
                log.info("Пользователь с id {} не найден", id);
            }

        } else if (id == 0) {
            userList = userRepository.findAll();
        }

        for (User user: userList) {
            userDtoList.add(UserMapper.toUserDto(user));
        }

        return userDtoList;
    }

    @Override
    public User getUserById(Long id) {
        User user;
        log.debug("Выполнен метод getUserShortDtoById({})", id);

        try {
            user = userRepository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }

        return user;

    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Выполнен метод deleteUserById({})", id);
        try {
            userRepository.deleteById(id);
        } catch (NoSuchElementException exception) {
            throw new ResponseStatusException(HttpStatus.resolve(404), "");
        }
    }

}
