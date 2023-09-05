package com.pblgllgs.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String path;
    private String exceptionName;
    private String message;
    private int status;
    private LocalDateTime localDateTime;
}