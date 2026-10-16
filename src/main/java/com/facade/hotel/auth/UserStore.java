package com.facade.hotel.auth;

import com.facade.hotel.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory user store keyed by lower-cased email.
 */
@Component
public class UserStore {

    private final ConcurrentMap<String, User> usersByEmail = new ConcurrentHashMap<>();

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(usersByEmail.get(email.toLowerCase()));
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public User save(User user) {
        usersByEmail.put(user.getEmail().toLowerCase(), user);
        return user;
    }
}
