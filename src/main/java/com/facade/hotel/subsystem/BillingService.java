package com.facade.hotel.subsystem;

import com.facade.hotel.domain.ExtraService;
import com.facade.hotel.domain.Invoice;
import com.facade.hotel.domain.InvoiceLine;
import com.facade.hotel.domain.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Subsystem 4 - Billing (FacturacionService).
 * Turns a reservation into a detailed invoice: room charge, each additional
 * service and, at checkout, an extra service/cleaning fee charged per night.
 */
@Service
public class BillingService {

    private static final BigDecimal SERVICE_FEE_PER_NIGHT = new BigDecimal("25000.00");

    public Invoice generate(Reservation reservation, boolean atCheckout) {
        List<InvoiceLine> lines = new ArrayList<>();

        lines.add(new InvoiceLine(
                "Habitacion " + reservation.getRoom().getId()
                        + " (" + reservation.getRoom().getType().getLabel() + ")",
                reservation.getNights(),
                reservation.getRoomCharge()
                        .divide(BigDecimal.valueOf(reservation.getNights()), 2, java.math.RoundingMode.HALF_UP)));

        for (ExtraService extra : reservation.getExtraServices()) {
            lines.add(new InvoiceLine(extra.getType().getLabel(), 1, extra.getCost()));
        }

        if (atCheckout) {
            lines.add(new InvoiceLine("Cargo de servicio (por noche)",
                    reservation.getNights(),
                    SERVICE_FEE_PER_NIGHT));
        }

        String number = "INV-" + Instant.now().toEpochMilli();
        return new Invoice(number, lines);
    }
}
