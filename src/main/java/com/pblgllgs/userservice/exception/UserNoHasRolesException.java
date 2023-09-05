package com.pblgllgs.userservice.exception;

public class UserNoHasRolesException extends RuntimeException{
    public UserNoHasRolesException(String message) {
        super(message);
    }
}
