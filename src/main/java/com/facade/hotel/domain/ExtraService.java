package com.facade.hotel.domain;

import java.math.BigDecimal;

/**
 * A concrete additional service added to a reservation, with its computed cost.
 */
public class ExtraService {

    private final ExtraServiceType type;
    private final BigDecimal cost;

    public ExtraService(ExtraServiceType type, int nights) {
        this.type = type;
        this.cost = type.costFor(nights);
    }

    public ExtraServiceType getType() {
        return type;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
