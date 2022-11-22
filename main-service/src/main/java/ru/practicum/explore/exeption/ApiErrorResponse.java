package ru.practicum.explore.exeption;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    //private List<Error> errors;
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
