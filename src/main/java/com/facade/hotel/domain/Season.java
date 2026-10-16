package com.facade.hotel.domain;

import java.math.BigDecimal;
import java.time.Month;
import java.time.MonthDay;
import java.util.Set;

/**
 * Hotel seasons with their nightly price multiplier.
 * High season (multiplier x1.5): December, January, June and July.
 * Low season (multiplier x1): the rest of the year.
 */
public enum Season {

    HIGH("Alta", new BigDecimal("1.5")),
    LOW("Baja", BigDecimal.ONE);

    private static final Set<Month> HIGH_SEASON_MONTHS =
            Set.of(Month.DECEMBER, Month.JANUARY, Month.JUNE, Month.JULY);

    private final String label;
    private final BigDecimal multiplier;

    Season(String label, BigDecimal multiplier) {
        this.label = label;
        this.multiplier = multiplier;
    }

    public static Season of(MonthDay date) {
        return HIGH_SEASON_MONTHS.contains(date.getMonth()) ? HIGH : LOW;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }
}
