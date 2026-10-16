package com.facade.hotel.persistence;

import com.facade.hotel.domain.ExtraServiceType;
import com.facade.hotel.domain.ReservationStatus;
import com.facade.hotel.domain.RoomType;
import com.facade.hotel.domain.Season;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity for reservations, including its extra services and final invoice.
 */
@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String guestName;

    private String guestEmail;
    private String guestDocument;

    @Column(nullable = false)
    private String roomId;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Enumerated(EnumType.STRING)
    private Season season;

    @Column(nullable = false)
    private BigDecimal roomCharge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private String accessKey;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_extras", joinColumns = @JoinColumn(name = "reservation_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "extra_type")
    private List<ExtraServiceType> extraTypes = new ArrayList<>();

    private String invoiceNumber;
    private Instant invoiceIssuedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_invoice_lines", joinColumns = @JoinColumn(name = "reservation_id"))
    private List<InvoiceLineEmbeddable> invoiceLines = new ArrayList<>();

    public ReservationEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public String getGuestDocument() {
        return guestDocument;
    }

    public void setGuestDocument(String guestDocument) {
        this.guestDocument = guestDocument;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public BigDecimal getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(BigDecimal roomCharge) {
        this.roomCharge = roomCharge;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public List<ExtraServiceType> getExtraTypes() {
        return extraTypes;
    }

    public void setExtraTypes(List<ExtraServiceType> extraTypes) {
        this.extraTypes = extraTypes;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Instant getInvoiceIssuedAt() {
        return invoiceIssuedAt;
    }

    public void setInvoiceIssuedAt(Instant invoiceIssuedAt) {
        this.invoiceIssuedAt = invoiceIssuedAt;
    }

    public List<InvoiceLineEmbeddable> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLineEmbeddable> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }
}
