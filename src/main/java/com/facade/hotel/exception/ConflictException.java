package com.facade.hotel.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends HotelException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
