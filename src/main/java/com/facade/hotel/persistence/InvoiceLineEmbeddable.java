package com.facade.hotel.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/**
 * Persistable invoice line.
 */
@Embeddable
public class InvoiceLineEmbeddable {

    private String concept;
    private int quantity;
    private BigDecimal unitPrice;

    public InvoiceLineEmbeddable() {
    }

    public InvoiceLineEmbeddable(String concept, int quantity, BigDecimal unitPrice) {
        this.concept = concept;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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
}
