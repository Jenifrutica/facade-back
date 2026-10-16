package com.facade.hotel.persistence;

import com.facade.hotel.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, String> {

    List<RoomEntity> findByType(RoomType type);
}
