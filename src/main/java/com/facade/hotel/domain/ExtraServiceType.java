package com.facade.hotel.domain;

import java.math.BigDecimal;

/**
 * Additional services a guest can add to a reservation.
 * Flat services are charged once; per-night services are multiplied by the stay length.
 */
public enum ExtraServiceType {

    SPA("Spa", new BigDecimal("50.00"), false),
    BREAKFAST("Desayuno", new BigDecimal("15.00"), true),
    TRANSFER("Traslado", new BigDecimal("30.00"), false);

    private final String label;
    private final BigDecimal unitPrice;
    private final boolean perNight;

    ExtraServiceType(String label, BigDecimal unitPrice, boolean perNight) {
        this.label = label;
        this.unitPrice = unitPrice;
        this.perNight = perNight;
    }

    public BigDecimal costFor(int nights) {
        return perNight ? unitPrice.multiply(BigDecimal.valueOf(nights)) : unitPrice;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public boolean isPerNight() {
        return perNight;
    }
}
