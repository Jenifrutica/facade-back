package com.facade.hotel.subsystem;

import com.facade.hotel.domain.Room;
import com.facade.hotel.domain.RoomType;
import com.facade.hotel.domain.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Subsystem 1 - Room inventory (HabitacionService).
 * Owns the predefined rooms and the in-memory reservation store. Knows how to
 * answer availability queries and how to persist/free reservations.
 * It has no knowledge of tariffs, billing or access keys.
 */
@Service
public class RoomService {

    private static final int SINGLE_ROOMS = 7;
    private static final int DOUBLE_ROOMS = 5;
    private static final int SUITE_ROOMS = 4;

    private final List<Room> rooms = new ArrayList<>();
    private final Map<String, Reservation> reservationsById = new LinkedHashMap<>();

    public RoomService() {
        seedRooms();
    }

    private void seedRooms() {
        for (int i = 1; i <= SINGLE_ROOMS; i++) {
            rooms.add(new Room(String.format("1%02d", i), RoomType.SINGLE));
        }
        for (int i = 1; i <= DOUBLE_ROOMS; i++) {
            rooms.add(new Room(String.format("2%02d", i), RoomType.DOUBLE));
        }
        for (int i = 1; i <= SUITE_ROOMS; i++) {
            rooms.add(new Room(String.format("3%02d", i), RoomType.SUITE));
        }
    }

    public List<Room> findAll() {
        return List.copyOf(rooms);
    }

    public List<Room> findByType(RoomType type) {
        return rooms.stream().filter(room -> room.getType() == type).toList();
    }

    /**
     * Rooms of the given type with no active reservation overlapping the requested range.
     */
    public List<Room> findAvailable(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        return rooms.stream()
                .filter(room -> room.getType() == type)
                .filter(room -> isAvailable(room, checkIn, checkOut))
                .toList();
    }

    public boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return reservationsById.values().stream()
                .filter(Reservation::isActive)
                .filter(reservation -> reservation.getRoom().getId().equals(room.getId()))
                .noneMatch(reservation -> reservation.overlaps(checkIn, checkOut));
    }

    public Optional<Room> findRoomById(String roomId) {
        return rooms.stream().filter(room -> room.getId().equals(roomId)).findFirst();
    }

    public Reservation save(Reservation reservation) {
        reservationsById.put(reservation.getId(), reservation);
        return reservation;
    }

    public Optional<Reservation> findReservation(String reservationId) {
        return Optional.ofNullable(reservationsById.get(reservationId));
    }

    public List<Reservation> findAllReservations() {
        return List.copyOf(reservationsById.values());
    }
}
