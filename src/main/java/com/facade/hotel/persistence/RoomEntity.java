package com.facade.hotel.persistence;

import com.facade.hotel.domain.RoomType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity for hotel rooms.
 */
@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;

    public RoomEntity() {
    }

    public RoomEntity(String id, RoomType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public RoomType getType() {
        return type;
    }
}
