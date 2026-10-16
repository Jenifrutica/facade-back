package com.facade.hotel.auth;

import com.facade.hotel.dto.LoginRequest;
import com.facade.hotel.dto.RegisterRequest;
import com.facade.hotel.exception.ConflictException;
import com.facade.hotel.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(new UserStore(), new BCryptPasswordEncoder(4),
                new JwtService("test-secret-key-for-unit-tests-only-1234"));
    }

    private RegisterRequest register(String email, String password) {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Jenifer Urbano");
        request.setEmail(email);
        request.setPassword(password);
        request.setConfirmPassword(password);
        return request;
    }

    @Test
    void registerHashesPasswordAndIssuesToken() {
        var response = authService.register(register("ana@ucc.edu.co", "Segura123"));
        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getUser().getEmail()).isEqualTo("ana@ucc.edu.co");
    }

    @Test
    void duplicateEmailIsRejected() {
        authService.register(register("ana@ucc.edu.co", "Segura123"));
        assertThatThrownBy(() -> authService.register(register("ana@ucc.edu.co", "Otra1234")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void loginWithWrongPasswordFails() {
        authService.register(register("ana@ucc.edu.co", "Segura123"));
        LoginRequest login = new LoginRequest();
        login.setEmail("ana@ucc.edu.co");
        login.setPassword("Incorrecta1");
        assertThatThrownBy(() -> authService.login(login))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void tokenResolvesCurrentUser() {
        var response = authService.register(register("ana@ucc.edu.co", "Segura123"));
        var user = authService.currentUser(response.getToken());
        assertThat(user.getFullName()).isEqualTo("Jenifer Urbano");
    }

    @Test
    void invalidTokenIsRejected() {
        assertThatThrownBy(() -> authService.currentUser("not.a.real.token"))
                .isInstanceOf(UnauthorizedException.class);
    }
}
