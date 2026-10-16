package com.facade.hotel.dto;

import java.math.BigDecimal;

/**
 * Additional service as shown in the reservation summary.
 */
public class ExtraServiceView {

    private final String type;
    private final String label;
    private final BigDecimal cost;

    public ExtraServiceView(String type, String label, BigDecimal cost) {
        this.type = type;
        this.label = label;
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
