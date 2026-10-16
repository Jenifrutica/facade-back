package com.facade.hotel.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Full reservation state returned by the facade to the frontend.
 */
public class ReservationView {

    private final String id;
    private final String guestName;
    private final String guestEmail;
    private final String guestDocument;
    private final String roomId;
    private final String roomType;
    private final String roomLabel;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int nights;
    private final String season;
    private final BigDecimal roomCharge;
    private final List<ExtraServiceView> extraServices;
    private final String status;
    private final String accessKey;
    private final InvoiceView invoice;

    public ReservationView(String id,
                           String guestName,
                           String guestEmail,
                           String guestDocument,
                           String roomId,
                           String roomType,
                           String roomLabel,
                           LocalDate checkIn,
                           LocalDate checkOut,
                           int nights,
                           String season,
                           BigDecimal roomCharge,
                           List<ExtraServiceView> extraServices,
                           String status,
                           String accessKey,
                           InvoiceView invoice) {
        this.id = id;
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestDocument = guestDocument;
        this.roomId = roomId;
        this.roomType = roomType;
        this.roomLabel = roomLabel;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.nights = nights;
        this.season = season;
        this.roomCharge = roomCharge;
        this.extraServices = extraServices;
        this.status = status;
        this.accessKey = accessKey;
        this.invoice = invoice;
    }

    public String getId() {
        return id;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public String getGuestDocument() {
        return guestDocument;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomLabel() {
        return roomLabel;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getNights() {
        return nights;
    }

    public String getSeason() {
        return season;
    }

    public BigDecimal getRoomCharge() {
        return roomCharge;
    }

    public List<ExtraServiceView> getExtraServices() {
        return extraServices;
    }

    public String getStatus() {
        return status;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public InvoiceView getInvoice() {
        return invoice;
    }
}
