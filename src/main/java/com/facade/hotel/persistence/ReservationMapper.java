package com.facade.hotel.persistence;

import com.facade.hotel.domain.ExtraService;
import com.facade.hotel.domain.ExtraServiceType;
import com.facade.hotel.domain.Invoice;
import com.facade.hotel.domain.InvoiceLine;
import com.facade.hotel.domain.Reservation;
import com.facade.hotel.domain.Room;

import java.util.List;

/**
 * Translates between the domain model (used by the facade and subsystems) and
 * the JPA entities (used by the database). Keeps the domain free of persistence
 * annotations.
 */
public final class ReservationMapper {

    private ReservationMapper() {
    }

    public static Room toDomain(RoomEntity entity) {
        return new Room(entity.getId(), entity.getType());
    }

    public static Reservation toDomain(ReservationEntity entity) {
        Room room = new Room(entity.getRoomId(), entity.getRoomType());
        Reservation reservation = new Reservation(
                entity.getId(),
                entity.getGuestName(),
                entity.getGuestEmail(),
                entity.getGuestDocument(),
                room,
                entity.getCheckIn(),
                entity.getCheckOut(),
                entity.getSeason(),
                entity.getRoomCharge());

        if (entity.getExtraTypes() != null) {
            for (ExtraServiceType type : entity.getExtraTypes()) {
                reservation.addExtraService(new ExtraService(type, reservation.getNights()));
            }
        }

        Invoice invoice = null;
        if (entity.getInvoiceNumber() != null) {
            List<InvoiceLine> lines = entity.getInvoiceLines().stream()
                    .map(line -> new InvoiceLine(line.getConcept(), line.getQuantity(), line.getUnitPrice()))
                    .toList();
            invoice = new Invoice(entity.getInvoiceNumber(), entity.getInvoiceIssuedAt(), lines);
        }

        reservation.restore(entity.getStatus(), entity.getAccessKey(), invoice);
        return reservation;
    }

    public static ReservationEntity toEntity(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setId(reservation.getId());
        entity.setGuestName(reservation.getGuestName());
        entity.setGuestEmail(reservation.getGuestEmail());
        entity.setGuestDocument(reservation.getGuestDocument());
        entity.setRoomId(reservation.getRoom().getId());
        entity.setRoomType(reservation.getRoom().getType());
        entity.setCheckIn(reservation.getCheckIn());
        entity.setCheckOut(reservation.getCheckOut());
        entity.setSeason(reservation.getSeason());
        entity.setRoomCharge(reservation.getRoomCharge());
        entity.setStatus(reservation.getStatus());
        entity.setAccessKey(reservation.getAccessKey());
        entity.setExtraTypes(reservation.getExtraServices().stream()
                .map(ExtraService::getType)
                .toList());

        if (reservation.getInvoice() != null) {
            Invoice invoice = reservation.getInvoice();
            entity.setInvoiceNumber(invoice.getNumber());
            entity.setInvoiceIssuedAt(invoice.getIssuedAt());
            entity.setInvoiceLines(invoice.getLines().stream()
                    .map(line -> new InvoiceLineEmbeddable(line.getConcept(), line.getQuantity(), line.getUnitPrice()))
                    .toList());
        }
        return entity;
    }
}
