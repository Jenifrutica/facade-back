package com.facade.hotel.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Final invoice as returned to the frontend.
 */
public class InvoiceView {

    public record Line(String concept, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
    }

    private final String number;
    private final Instant issuedAt;
    private final List<Line> lines;
    private final BigDecimal total;

    public InvoiceView(String number, Instant issuedAt, List<Line> lines, BigDecimal total) {
        this.number = number;
        this.issuedAt = issuedAt;
        this.lines = lines;
        this.total = total;
    }

    public String getNumber() {
        return number;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public List<Line> getLines() {
        return lines;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
