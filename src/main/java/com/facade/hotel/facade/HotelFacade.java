package com.facade.hotel.facade;

import com.facade.hotel.domain.ExtraService;
import com.facade.hotel.domain.ExtraServiceType;
import com.facade.hotel.domain.Invoice;
import com.facade.hotel.domain.RateQuote;
import com.facade.hotel.domain.Reservation;
import com.facade.hotel.domain.ReservationStatus;
import com.facade.hotel.domain.Room;
import com.facade.hotel.domain.RoomType;
import com.facade.hotel.dto.ExtraServiceView;
import com.facade.hotel.dto.InvoiceView;
import com.facade.hotel.dto.ReservationRequest;
import com.facade.hotel.dto.ReservationView;
import com.facade.hotel.dto.RoomView;
import com.facade.hotel.exception.BadRequestException;
import com.facade.hotel.exception.ConflictException;
import com.facade.hotel.exception.NotFoundException;
import com.facade.hotel.subsystem.AccessService;
import com.facade.hotel.subsystem.BillingService;
import com.facade.hotel.subsystem.ExtraServiceService;
import com.facade.hotel.subsystem.RateService;
import com.facade.hotel.subsystem.RoomService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * HOTEL FACADE - the single, simplified entry point to the whole hotel subsystem.
 *
 * The client (REST controller / frontend) never talks to the five subsystems
 * directly. It calls one readable operation and the facade coordinates inventory,
 * tariffs, extra services, billing and access control behind the scenes.
 */
@Component
public class HotelFacade {

    private final RoomService roomService;
    private final RateService rateService;
    private final ExtraServiceService extraServiceService;
    private final BillingService billingService;
    private final AccessService accessService;

    public HotelFacade(RoomService roomService,
                       RateService rateService,
                       ExtraServiceService extraServiceService,
                       BillingService billingService,
                       AccessService accessService) {
        this.roomService = roomService;
        this.rateService = rateService;
        this.extraServiceService = extraServiceService;
        this.billingService = billingService;
        this.accessService = accessService;
    }

    // ---------------------------------------------------------------------
    // Availability
    // ---------------------------------------------------------------------

    /**
     * Returns free rooms (optionally filtered by type) with the dynamic price for the stay.
     */
    public List<RoomView> findAvailability(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        LocalDate from = checkIn != null ? checkIn : LocalDate.now();
        LocalDate to = checkOut != null ? checkOut : from.plusDays(1);
        validateRange(from, to);

        List<Room> candidates = type == null ? roomService.findAll() : roomService.findByType(type);
        List<RoomView> result = new ArrayList<>();
        for (Room room : candidates) {
            if (roomService.isAvailable(room, from, to)) {
                result.add(toRoomView(room, from, to));
            }
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // Reservation lifecycle
    // ---------------------------------------------------------------------

    /**
     * Creates a reservation: picks a free room of the requested type and stores it.
     */
    public ReservationView createReservation(ReservationRequest request) {
        validateRange(request.getCheckIn(), request.getCheckOut());

        List<Room> available = roomService.findAvailable(
                request.getRoomType(), request.getCheckIn(), request.getCheckOut());
        if (available.isEmpty()) {
            throw new ConflictException("No hay habitaciones disponibles de tipo "
                    + request.getRoomType().getLabel() + " para esas fechas");
        }

        Room room = available.get(0);
        RateQuote quote = rateService.quote(room, request.getCheckIn(), request.getCheckOut());
        Reservation reservation = new Reservation(
                request.getGuestName(),
                request.getGuestEmail(),
                request.getGuestDocument(),
                room,
                request.getCheckIn(),
                request.getCheckOut(),
                quote.getSeason(),
                quote.getRoomCharge());
        roomService.save(reservation);
        return toView(reservation);
    }

    /**
     * Adds an additional service to a reservation still in progress.
     */
    public ReservationView addExtraService(String reservationId, ExtraServiceType type) {
        Reservation reservation = requireReservation(reservationId);
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new ConflictException("No se pueden agregar servicios a una reserva finalizada");
        }
        ExtraService service = extraServiceService.build(type, reservation.getNights());
        reservation.addExtraService(service);
        roomService.save(reservation);
        return toView(reservation);
    }

    /**
     * Check-in: marks the stay as active and issues the digital access key.
     */
    public ReservationView checkIn(String reservationId) {
        Reservation reservation = requireReservation(reservationId);
        if (reservation.getStatus() != ReservationStatus.CREATED) {
            throw new ConflictException("La reserva no esta en estado CREADA");
        }
        reservation.assignAccessKey(accessService.issueKey());
        reservation.checkIn();
        roomService.save(reservation);
        return toView(reservation);
    }

    /**
     * Check-out: computes the final charges (including the per-night service fee),
     * generates the final invoice and frees the room.
     */
    public ReservationView checkOut(String reservationId) {
        Reservation reservation = requireReservation(reservationId);
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new ConflictException("La reserva debe estar en estado EN HOTEL para hacer check-out");
        }
        Invoice invoice = billingService.generate(reservation, true);
        reservation.checkOut(invoice);
        roomService.save(reservation);
        return toView(reservation);
    }

    // ---------------------------------------------------------------------
    // Queries / catalog
    // ---------------------------------------------------------------------

    public ReservationView getReservation(String reservationId) {
        return toView(requireReservation(reservationId));
    }

    public List<ReservationView> listReservations() {
        return roomService.findAllReservations().stream().map(this::toView).toList();
    }

    public List<ExtraServiceType> extraServices() {
        return extraServiceService.catalog();
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private Reservation requireReservation(String reservationId) {
        return roomService.findReservation(reservationId)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada: " + reservationId));
    }

    private void validateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new BadRequestException("Las fechas de entrada y salida son obligatorias");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new BadRequestException("La fecha de salida debe ser posterior a la de entrada");
        }
        if (checkIn.isAfter(LocalDate.now().plusYears(1))) {
            throw new BadRequestException("La fecha de entrada no puede superar un ano desde hoy");
        }
    }

    private RoomView toRoomView(Room room, LocalDate checkIn, LocalDate checkOut) {
        RateQuote quote = rateService.quote(room, checkIn, checkOut);
        return new RoomView(
                room.getId(),
                room.getType(),
                room.getType().getLabel(),
                room.getBasePrice(),
                quote.getNights(),
                quote.getSeason().getLabel(),
                quote.getSeason().getMultiplier(),
                quote.getRoomCharge());
    }

    private ReservationView toView(Reservation reservation) {
        List<ExtraServiceView> extras = reservation.getExtraServices().stream()
                .map(extra -> new ExtraServiceView(
                        extra.getType().name(),
                        extra.getType().getLabel(),
                        extra.getCost()))
                .toList();

        InvoiceView invoiceView = null;
        if (reservation.getInvoice() != null) {
            Invoice invoice = reservation.getInvoice();
            List<InvoiceView.Line> lines = invoice.getLines().stream()
                    .map(line -> new InvoiceView.Line(
                            line.getConcept(), line.getQuantity(), line.getUnitPrice(), line.getSubtotal()))
                    .toList();
            invoiceView = new InvoiceView(invoice.getNumber(), invoice.getIssuedAt(), lines, invoice.getTotal());
        }

        return new ReservationView(
                reservation.getId(),
                reservation.getGuestName(),
                reservation.getGuestEmail(),
                reservation.getGuestDocument(),
                reservation.getRoom().getId(),
                reservation.getRoom().getType().name(),
                reservation.getRoom().getType().getLabel(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getNights(),
                reservation.getSeason().getLabel(),
                reservation.getRoomCharge(),
                extras,
                reservation.getStatus().name(),
                reservation.getAccessKey(),
                invoiceView);
    }
}
