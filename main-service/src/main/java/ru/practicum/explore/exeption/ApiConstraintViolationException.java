package ru.practicum.explore.exeption;

public class ApiConstraintViolationException extends RuntimeException {
    public ApiConstraintViolationException(String message) {
        super(message);
    }
}
