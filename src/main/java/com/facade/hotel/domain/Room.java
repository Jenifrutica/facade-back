package com.facade.hotel.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A physical room of the hotel. It knows nothing about reservations, tariffs or
 * billing; those concerns belong to the subsystems.
 */
public class Room {

    private final String id;
    private final RoomType type;
    private final BigDecimal basePrice;

    public Room(String id, RoomType type) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = Objects.requireNonNull(type, "type");
        this.basePrice = type.getBasePrice();
    }

    public String getId() {
        return id;
    }

    public RoomType getType() {
        return type;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
}
