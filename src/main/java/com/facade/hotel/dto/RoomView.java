package com.facade.hotel.dto;

import com.facade.hotel.domain.RoomType;

import java.math.BigDecimal;

/**
 * Availability row: a free room plus the dynamic price for the requested stay,
 * so the frontend can preview the price before reserving.
 */
public class RoomView {

    private final String id;
    private final RoomType type;
    private final String label;
    private final BigDecimal basePrice;
    private final int nights;
    private final String season;
    private final BigDecimal seasonMultiplier;
    private final BigDecimal totalPrice;

    public RoomView(String id,
                    RoomType type,
                    String label,
                    BigDecimal basePrice,
                    int nights,
                    String season,
                    BigDecimal seasonMultiplier,
                    BigDecimal totalPrice) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.basePrice = basePrice;
        this.nights = nights;
        this.season = season;
        this.seasonMultiplier = seasonMultiplier;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public RoomType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public int getNights() {
        return nights;
    }

    public String getSeason() {
        return season;
    }

    public BigDecimal getSeasonMultiplier() {
        return seasonMultiplier;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
