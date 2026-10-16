package com.facade.hotel.domain;

import java.math.BigDecimal;

/**
 * Room categories offered by the hotel. Each type carries its own base nightly price.
 */
public enum RoomType {

    SINGLE("Sencilla", new BigDecimal("180000.00")),
    DOUBLE("Doble", new BigDecimal("300000.00")),
    SUITE("Suite", new BigDecimal("480000.00"));

    private final String label;
    private final BigDecimal basePrice;

    RoomType(String label, BigDecimal basePrice) {
        this.label = label;
        this.basePrice = basePrice;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
}
