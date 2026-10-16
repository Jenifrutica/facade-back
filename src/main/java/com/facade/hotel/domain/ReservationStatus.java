package com.facade.hotel.domain;

/**
 * Lifecycle of a reservation as it moves through the hotel operation.
 */
public enum ReservationStatus {

    CREATED("Creada"),
    CHECKED_IN("En hotel"),
    CHECKED_OUT("Finalizada");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
