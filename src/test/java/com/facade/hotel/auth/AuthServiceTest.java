package com.facade.hotel.auth;

import com.facade.hotel.dto.LoginRequest;
import com.facade.hotel.dto.RegisterRequest;
import com.facade.hotel.exception.ConflictException;
import com.facade.hotel.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    private RegisterRequest register(String email, String password) {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Jenifer Urbano");
        request.setEmail(email);
        request.setPassword(password);
        request.setConfirmPassword(password);
        return request;
    }

    private String uniqueEmail() {
        return "user-" + UUID.randomUUID() + "@ucc.edu.co";
    }

    @Test
    void registerHashesPasswordAndIssuesToken() {
        String email = uniqueEmail();
        var response = authService.register(register(email, "Segura123"));
        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getUser().getEmail()).isEqualTo(email);
    }

    @Test
    void duplicateEmailIsRejected() {
        String email = uniqueEmail();
        authService.register(register(email, "Segura123"));
        assertThatThrownBy(() -> authService.register(register(email, "Otra1234")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void loginWithWrongPasswordFails() {
        String email = uniqueEmail();
        authService.register(register(email, "Segura123"));
        LoginRequest login = new LoginRequest();
        login.setEmail(email);
        login.setPassword("Incorrecta1");
        assertThatThrownBy(() -> authService.login(login))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void tokenResolvesCurrentUser() {
        var response = authService.register(register(uniqueEmail(), "Segura123"));
        var user = authService.currentUser(response.getToken());
        assertThat(user.getFullName()).isEqualTo("Jenifer Urbano");
    }

    @Test
    void invalidTokenIsRejected() {
        assertThatThrownBy(() -> authService.currentUser("not.a.real.token"))
                .isInstanceOf(UnauthorizedException.class);
    }
}
