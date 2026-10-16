package com.facade.hotel.auth;

import com.facade.hotel.domain.User;
import com.facade.hotel.persistence.UserEntity;
import com.facade.hotel.persistence.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * User store backed by the database (H2 via Spring Data JPA).
 */
@Component
public class UserStore {

    private final UserJpaRepository users;

    public UserStore(UserJpaRepository users) {
        this.users = users;
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return users.findByEmail(email.toLowerCase()).map(this::toDomain);
    }

    public boolean existsByEmail(String email) {
        return users.existsByEmail(email == null ? null : email.toLowerCase());
    }

    public User save(User user) {
        users.save(new UserEntity(
                user.getId(), user.getFullName(), user.getEmail(), user.getPasswordHash(), user.getCreatedAt()));
        return user;
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(), entity.getFullName(), entity.getEmail(), entity.getPasswordHash(), entity.getCreatedAt());
    }
}
