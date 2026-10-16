package com.facade.hotel.domain;

import java.math.BigDecimal;

/**
 * A single line of an invoice: concept, quantity, unit price and resulting subtotal.
 */
public class InvoiceLine {

    private final String concept;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal subtotal;

    public InvoiceLine(String concept, int quantity, BigDecimal unitPrice) {
        this.concept = concept;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public String getConcept() {
        return concept;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
