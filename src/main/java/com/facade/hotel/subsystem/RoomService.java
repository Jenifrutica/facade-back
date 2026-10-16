package com.facade.hotel.subsystem;

import com.facade.hotel.domain.Reservation;
import com.facade.hotel.domain.ReservationStatus;
import com.facade.hotel.domain.Room;
import com.facade.hotel.domain.RoomType;
import com.facade.hotel.persistence.ReservationJpaRepository;
import com.facade.hotel.persistence.ReservationMapper;
import com.facade.hotel.persistence.RoomJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Subsystem 1 - Room inventory (HabitacionService).
 * Owns rooms and reservations through the database. Knows how to answer
 * availability queries and how to persist/free reservations. It has no
 * knowledge of tariffs, billing or access keys.
 */
@Service
public class RoomService {

    private final RoomJpaRepository rooms;
    private final ReservationJpaRepository reservations;

    public RoomService(RoomJpaRepository rooms, ReservationJpaRepository reservations) {
        this.rooms = rooms;
        this.reservations = reservations;
    }

    public List<Room> findAll() {
        return rooms.findAll().stream().map(ReservationMapper::toDomain).toList();
    }

    public List<Room> findByType(RoomType type) {
        return rooms.findByType(type).stream().map(ReservationMapper::toDomain).toList();
    }

    /**
     * Rooms of the given type with no active reservation overlapping the requested range.
     */
    public List<Room> findAvailable(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        return findByType(type).stream()
                .filter(room -> isAvailable(room, checkIn, checkOut))
                .toList();
    }

    public boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return reservations.findByRoomIdAndStatusNot(room.getId(), ReservationStatus.CHECKED_OUT).stream()
                .map(ReservationMapper::toDomain)
                .noneMatch(reservation -> reservation.overlaps(checkIn, checkOut));
    }

    public Optional<Room> findRoomById(String roomId) {
        return rooms.findById(roomId).map(ReservationMapper::toDomain);
    }

    public Reservation save(Reservation reservation) {
        reservations.save(ReservationMapper.toEntity(reservation));
        return reservation;
    }

    public Optional<Reservation> findReservation(String reservationId) {
        return reservations.findById(reservationId).map(ReservationMapper::toDomain);
    }

    public List<Reservation> findAllReservations() {
        return reservations.findAll().stream().map(ReservationMapper::toDomain).toList();
    }
}
