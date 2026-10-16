package com.facade.hotel.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception carrying the HTTP status the API should answer with.
 */
public class HotelException extends RuntimeException {

    private final HttpStatus status;

    public HotelException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
