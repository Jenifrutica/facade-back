package com.facade.hotel.domain;

import java.math.BigDecimal;

/**
 * Result of a tariff calculation: number of nights, applied season and total room charge.
 */
public class RateQuote {

    private final int nights;
    private final Season season;
    private final BigDecimal roomCharge;

    public RateQuote(int nights, Season season, BigDecimal roomCharge) {
        this.nights = nights;
        this.season = season;
        this.roomCharge = roomCharge;
    }

    public int getNights() {
        return nights;
    }

    public Season getSeason() {
        return season;
    }

    public BigDecimal getRoomCharge() {
        return roomCharge;
    }
}
