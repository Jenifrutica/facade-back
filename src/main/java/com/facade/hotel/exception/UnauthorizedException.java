package com.facade.hotel.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HotelException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
