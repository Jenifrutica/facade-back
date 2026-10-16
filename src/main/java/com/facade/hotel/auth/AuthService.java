package com.facade.hotel.auth;

import com.facade.hotel.domain.User;
import com.facade.hotel.dto.AuthResponse;
import com.facade.hotel.dto.LoginRequest;
import com.facade.hotel.dto.RegisterRequest;
import com.facade.hotel.dto.UserView;
import com.facade.hotel.exception.ConflictException;
import com.facade.hotel.exception.UnauthorizedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles registration and login. Passwords are stored only as BCrypt hashes.
 */
@Service
public class AuthService {

    private final UserStore userStore;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserStore userStore, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userStore = userStore;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userStore.existsByEmail(request.getEmail())) {
            throw new ConflictException("El correo ya esta registrado");
        }
        String hash = passwordEncoder.encode(request.getPassword());
        User user = userStore.save(new User(request.getFullName().trim(), request.getEmail().trim().toLowerCase(), hash));
        return buildResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userStore.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Credenciales invalidas");
        }
        return buildResponse(user);
    }

    public UserView currentUser(String token) {
        String email = jwtService.validateAndGetEmail(token);
        if (email == null) {
            throw new UnauthorizedException("Token invalido o expirado");
        }
        User user = userStore.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));
        return toView(user);
    }

    public String emailFromToken(String token) {
        return jwtService.validateAndGetEmail(token);
    }

    private AuthResponse buildResponse(User user) {
        String token = jwtService.issue(user.getEmail());
        return new AuthResponse(token, toView(user));
    }

    private UserView toView(User user) {
        return new UserView(user.getId(), user.getFullName(), user.getEmail());
    }
}
