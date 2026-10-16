package com.facade.hotel.persistence;

import com.facade.hotel.domain.RoomType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the hotel's 16 predefined rooms the first time the database is empty.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final int SINGLE_ROOMS = 7;
    private static final int DOUBLE_ROOMS = 5;
    private static final int SUITE_ROOMS = 4;

    private final RoomJpaRepository rooms;

    public DataInitializer(RoomJpaRepository rooms) {
        this.rooms = rooms;
    }

    @Override
    public void run(String... args) {
        if (rooms.count() > 0) {
            return;
        }
        for (int i = 1; i <= SINGLE_ROOMS; i++) {
            rooms.save(new RoomEntity(String.format("1%02d", i), RoomType.SINGLE));
        }
        for (int i = 1; i <= DOUBLE_ROOMS; i++) {
            rooms.save(new RoomEntity(String.format("2%02d", i), RoomType.DOUBLE));
        }
        for (int i = 1; i <= SUITE_ROOMS; i++) {
            rooms.save(new RoomEntity(String.format("3%02d", i), RoomType.SUITE));
        }
    }
}
