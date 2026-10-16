package com.facade.hotel.persistence;

import com.facade.hotel.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, String> {

    List<ReservationEntity> findByRoomIdAndStatusNot(String roomId, ReservationStatus status);

    List<ReservationEntity> findByStatusNot(ReservationStatus status);
}
