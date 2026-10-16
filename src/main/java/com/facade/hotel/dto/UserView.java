package com.facade.hotel.dto;

/**
 * Authenticated user profile returned to the frontend (never includes the hash).
 */
public class UserView {

    private final String id;
    private final String fullName;
    private final String email;

    public UserView(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
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
}
