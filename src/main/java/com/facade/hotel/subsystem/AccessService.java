package com.facade.hotel.subsystem;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Subsystem 5 - Access control (AccesoService).
 * Issues simulated digital keys (UUID) for reserved rooms.
 */
@Service
public class AccessService {

    public String issueKey() {
        return UUID.randomUUID().toString();
    }
}
