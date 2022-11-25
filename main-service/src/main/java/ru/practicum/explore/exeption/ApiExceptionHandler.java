package ru.practicum.explore.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handle(NoSuchElementException ex) {
        log.error("Storage error - object not found" + "\n" + ex.getMessage());
        return new ApiErrorResponse(
                ex.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString()
                );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handle(ApiStorageException ex) {
        log.error(ex.getMessage());
        return new ApiErrorResponse(
                ex.getMessage(),
                "Integrity constraint has been violated",
                "CONFLICT",
                LocalDateTime.now().toString()
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(ApiConstraintViolationException ex) {
        log.error(ex.getMessage());
        return new ApiErrorResponse(
                ex.getMessage(),
                "For the requested operation the conditions are not met.",
                "BAD_REQUEST",
                LocalDateTime.now().toString()
        );
    }




}
