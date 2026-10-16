package com.facade.hotel.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate that represents a booking: guest data, stay dates, selected room,
 * additional services, digital access key and the final invoice.
 */
public class Reservation {

    private final String id;
    private final String guestName;
    private final String guestEmail;
    private final String guestDocument;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int nights;
    private final Season season;
    private final BigDecimal roomCharge;
    private final List<ExtraService> extraServices = new ArrayList<>();
    private ReservationStatus status;
    private String accessKey;
    private Invoice invoice;

    public Reservation(String guestName,
                       String guestEmail,
                       String guestDocument,
                       Room room,
                       LocalDate checkIn,
                       LocalDate checkOut,
                       Season season,
                       BigDecimal roomCharge) {
        this.id = UUID.randomUUID().toString();
        this.guestName = Objects.requireNonNull(guestName, "guestName");
        this.guestEmail = guestEmail;
        this.guestDocument = guestDocument;
        this.room = Objects.requireNonNull(room, "room");
        this.checkIn = Objects.requireNonNull(checkIn, "checkIn");
        this.checkOut = Objects.requireNonNull(checkOut, "checkOut");
        this.nights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        this.season = season;
        this.roomCharge = roomCharge;
        this.status = ReservationStatus.CREATED;
    }

    /**
     * Two reservations overlap when their date ranges intersect (checkout day is exclusive).
     */
    public boolean overlaps(LocalDate otherCheckIn, LocalDate otherCheckOut) {
        return checkIn.isBefore(otherCheckOut) && otherCheckIn.isBefore(checkOut);
    }

    public boolean isActive() {
        return status != ReservationStatus.CHECKED_OUT;
    }

    public void addExtraService(ExtraService service) {
        extraServices.add(service);
    }

    public void assignAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void checkIn() {
        this.status = ReservationStatus.CHECKED_IN;
    }

    public void checkOut(Invoice invoice) {
        this.invoice = invoice;
        this.status = ReservationStatus.CHECKED_OUT;
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

    public Room getRoom() {
        return room;
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

    public Season getSeason() {
        return season;
    }

    public BigDecimal getRoomCharge() {
        return roomCharge;
    }

    public List<ExtraService> getExtraServices() {
        return Collections.unmodifiableList(extraServices);
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public Invoice getInvoice() {
        return invoice;
    }
}
