package com.facade.hotel.facade;

import com.facade.hotel.domain.ExtraServiceType;
import com.facade.hotel.domain.RoomType;
import com.facade.hotel.dto.ReservationRequest;
import com.facade.hotel.dto.ReservationView;
import com.facade.hotel.dto.RoomView;
import com.facade.hotel.exception.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class HotelFacadeTest {

    @Autowired
    private HotelFacade facade;

    private ReservationRequest request(RoomType type, LocalDate in, LocalDate out) {
        ReservationRequest request = new ReservationRequest();
        request.setGuestName("Ana Torres");
        request.setGuestEmail("ana@example.com");
        request.setGuestDocument("10203040");
        request.setRoomType(type);
        request.setCheckIn(in);
        request.setCheckOut(out);
        return request;
    }

    @Test
    void lowSeasonChargeEqualsBasePriceTimesNights() {
        // March is low season (multiplier x1)
        LocalDate in = LocalDate.of(2027, 3, 10);
        LocalDate out = LocalDate.of(2027, 3, 13);
        ReservationView view = facade.createReservation(request(RoomType.SINGLE, in, out));

        assertThat(view.getNights()).isEqualTo(3);
        assertThat(view.getSeason()).isEqualTo("Baja");
        assertThat(view.getRoomCharge()).isEqualByComparingTo(new BigDecimal("540000.00"));
        assertThat(view.getStatus()).isEqualTo("CREATED");
    }

    @Test
    void highSeasonAppliesMultiplierOfOnePointFive() {
        // July is high season (multiplier x1.5)
        LocalDate in = LocalDate.of(2027, 7, 1);
        LocalDate out = LocalDate.of(2027, 7, 3);
        ReservationView view = facade.createReservation(request(RoomType.DOUBLE, in, out));

        assertThat(view.getSeason()).isEqualTo("Alta");
        assertThat(view.getRoomCharge()).isEqualByComparingTo(new BigDecimal("900000.00"));
    }

    @Test
    void availabilityOnlyReturnsRequestedType() {
        List<RoomView> available = facade.findAvailability(
                RoomType.SUITE, LocalDate.of(2027, 2, 1), LocalDate.of(2027, 2, 2));
        assertThat(available).isNotEmpty();
        assertThat(available).allMatch(room -> room.getType() == RoomType.SUITE);
    }

    @Test
    void addingBreakfastChargesPerNight() {
        ReservationView created = facade.createReservation(
                request(RoomType.SINGLE, LocalDate.of(2027, 4, 1), LocalDate.of(2027, 4, 5)));
        ReservationView withService = facade.addExtraService(created.getId(), ExtraServiceType.BREAKFAST);

        assertThat(withService.getExtraServices()).hasSize(1);
        // 4 nights x 15 COP
        assertThat(withService.getExtraServices().get(0).getCost())
                .isEqualByComparingTo(new BigDecimal("60.00"));
    }

    @Test
    void fullLifecycleCreatesAccessKeyAndFinalInvoice() {
        ReservationView created = facade.createReservation(
                request(RoomType.SUITE, LocalDate.of(2027, 5, 1), LocalDate.of(2027, 5, 3)));
        assertThat(created.getAccessKey()).isNull();

        ReservationView checkedIn = facade.checkIn(created.getId());
        assertThat(checkedIn.getStatus()).isEqualTo("CHECKED_IN");
        assertThat(checkedIn.getAccessKey()).isNotBlank();

        facade.addExtraService(created.getId(), ExtraServiceType.SPA);
        ReservationView checkedOut = facade.checkOut(created.getId());

        assertThat(checkedOut.getStatus()).isEqualTo("CHECKED_OUT");
        assertThat(checkedOut.getInvoice()).isNotNull();
        assertThat(checkedOut.getInvoice().getTotal()).isPositive();

        // Persisted state is rehydrated correctly.
        ReservationView reloaded = facade.getReservation(created.getId());
        assertThat(reloaded.getStatus()).isEqualTo("CHECKED_OUT");
        assertThat(reloaded.getInvoice().getNumber()).isEqualTo(checkedOut.getInvoice().getNumber());
    }

    @Test
    void cannotCheckOutBeforeCheckIn() {
        ReservationView created = facade.createReservation(
                request(RoomType.SINGLE, LocalDate.of(2027, 8, 1), LocalDate.of(2027, 8, 2)));

        assertThatThrownBy(() -> facade.checkOut(created.getId()))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void roomCannotBeDoubleBookedForSameDates() {
        LocalDate in = LocalDate.of(2027, 9, 1);
        LocalDate out = LocalDate.of(2027, 9, 4);
        // Exhaust the four suites with overlapping reservations.
        for (int i = 0; i < 4; i++) {
            facade.createReservation(request(RoomType.SUITE, in, out));
        }
        assertThatThrownBy(() -> facade.createReservation(request(RoomType.SUITE, in, out)))
                .isInstanceOf(ConflictException.class);
    }
}
