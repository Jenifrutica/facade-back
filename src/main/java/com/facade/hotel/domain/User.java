package com.facade.hotel.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Application user with a BCrypt-hashed password.
 */
public class User {

    private final String id;
    private final String fullName;
    private final String email;
    private final String passwordHash;
    private final Instant createdAt;

    public User(String fullName, String email, String passwordHash) {
        this(UUID.randomUUID().toString(), fullName, email, passwordHash, Instant.now());
    }

    public User(String id, String fullName, String email, String passwordHash, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
