package ru.practicum.explore.exeption;

public class ApiStorageException extends RuntimeException {
    public ApiStorageException(String message) {
        super(message);
    }
}