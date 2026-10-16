package com.facade.hotel.dto;

import com.facade.hotel.domain.RoomType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Incoming payload to create a reservation through the facade.
 */
public class ReservationRequest {

    @NotBlank(message = "El nombre del huesped es obligatorio")
    @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres")
    private String guestName;

    @Email(message = "El correo no es valido")
    @Size(max = 254, message = "El correo no puede superar 254 caracteres")
    private String guestEmail;

    @Size(max = 30, message = "El documento no puede superar 30 caracteres")
    private String guestDocument;

    @NotNull(message = "El tipo de habitacion es obligatorio")
    private RoomType roomType;

    @NotNull(message = "La fecha de entrada es obligatoria")
    @FutureOrPresent(message = "La fecha de entrada no puede ser pasada")
    private LocalDate checkIn;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate checkOut;

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
}
