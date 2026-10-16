package com.facade.hotel.controller;

import com.facade.hotel.domain.ExtraServiceType;
import com.facade.hotel.domain.RoomType;
import com.facade.hotel.dto.AddServiceRequest;
import com.facade.hotel.dto.ReservationRequest;
import com.facade.hotel.dto.ReservationView;
import com.facade.hotel.dto.RoomView;
import com.facade.hotel.facade.HotelFacade;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Thin REST layer. Every endpoint delegates to the HotelFacade; the controller
 * never talks to the subsystems directly.
 */
@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelFacade hotelFacade;

    public HotelController(HotelFacade hotelFacade) {
        this.hotelFacade = hotelFacade;
    }

    @GetMapping("/disponibilidad")
    public List<RoomView> availability(
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return hotelFacade.findAvailability(type, checkIn, checkOut);
    }

    @PostMapping("/reservar")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationView reserve(@Valid @RequestBody ReservationRequest request) {
        return hotelFacade.createReservation(request);
    }

    @PostMapping("/servicios/{reservaId}")
    public ReservationView addService(@PathVariable String reservaId,
                                      @Valid @RequestBody AddServiceRequest request) {
        return hotelFacade.addExtraService(reservaId, request.getType());
    }

    @PutMapping("/checkin/{reservaId}")
    public ReservationView checkIn(@PathVariable String reservaId) {
        return hotelFacade.checkIn(reservaId);
    }

    @PutMapping("/checkout/{reservaId}")
    public ReservationView checkOut(@PathVariable String reservaId) {
        return hotelFacade.checkOut(reservaId);
    }

    @GetMapping("/reserva/{reservaId}")
    public ReservationView getReservation(@PathVariable String reservaId) {
        return hotelFacade.getReservation(reservaId);
    }

    @GetMapping("/reservas")
    public List<ReservationView> listReservations() {
        return hotelFacade.listReservations();
    }

    @GetMapping("/servicios")
    public List<ExtraServiceType> services() {
        return hotelFacade.extraServices();
    }
}
