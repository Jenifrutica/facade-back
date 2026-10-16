package com.facade.hotel.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Detailed, immutable-once-issued invoice produced by the billing subsystem.
 */
public class Invoice {

    private final String number;
    private final Instant issuedAt;
    private final List<InvoiceLine> lines;
    private final BigDecimal total;

    public Invoice(String number, List<InvoiceLine> lines) {
        this.number = number;
        this.issuedAt = Instant.now();
        this.lines = new ArrayList<>(lines);
        this.total = this.lines.stream()
                .map(InvoiceLine::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getNumber() {
        return number;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public List<InvoiceLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public BigDecimal getTotal() {
        return total;
    }
}
