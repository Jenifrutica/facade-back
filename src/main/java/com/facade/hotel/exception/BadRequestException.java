package com.facade.hotel.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HotelException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
