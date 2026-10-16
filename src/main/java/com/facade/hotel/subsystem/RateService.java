package com.facade.hotel.subsystem;

import com.facade.hotel.domain.RateQuote;
import com.facade.hotel.domain.Room;
import com.facade.hotel.domain.Season;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Subsystem 2 - Dynamic tariff (TarifaService).
 * Computes nights, applies the season multiplier (high x1.5, low x1) and returns
 * the total room charge. Stateless.
 */
@Service
public class RateService {

    private static final BigDecimal SERVICE_ROUNDING = BigDecimal.valueOf(2);

    public RateQuote quote(Room room, LocalDate checkIn, LocalDate checkOut) {
        int nights = nightsBetween(checkIn, checkOut);
        Season season = Season.of(monthDay(checkIn));
        BigDecimal charge = room.getBasePrice()
                .multiply(season.getMultiplier())
                .multiply(BigDecimal.valueOf(nights))
                .setScale(SERVICE_ROUNDING.intValue(), RoundingMode.HALF_UP);
        return new RateQuote(nights, season, charge);
    }

    private java.time.MonthDay monthDay(LocalDate date) {
        return java.time.MonthDay.from(date);
    }

    public int nightsBetween(LocalDate checkIn, LocalDate checkOut) {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}
