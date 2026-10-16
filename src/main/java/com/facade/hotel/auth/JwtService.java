package com.facade.hotel.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

/**
 * Minimal, dependency-free JWT-like token service (HMAC-SHA256).
 * Token format: base64url(payload) "." base64url(hmac(payload)), where
 * payload is "email;expiryEpochMillis".
 */
@Component
public class JwtService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final long TTL_MILLIS = 7L * 24 * 60 * 60 * 1000;

    private final byte[] secret;

    public JwtService(@Value("${app.jwt.secret:dev-secret-change-me-please-32bytes-min}") String secret) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    public String issue(String email) {
        long expiry = Instant.now().toEpochMilli() + TTL_MILLIS;
        String payload = email + ";" + expiry;
        String encodedPayload = encode(payload);
        return encodedPayload + "." + encode(hmac(encodedPayload));
    }

    /**
     * @return the email carried by the token, or {@code null} if invalid/expired.
     */
    public String validateAndGetEmail(String token) {
        if (token == null || !token.contains(".")) {
            return null;
        }
        String[] parts = token.split("\\.", 2);
        String expected = encode(hmac(parts[0]));
        if (!MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),
                parts[1].getBytes(StandardCharsets.UTF_8))) {
            return null;
        }
        String payload = decode(parts[0]);
        String[] payloadParts = payload.split(";", 2);
        if (payloadParts.length != 2) {
            return null;
        }
        try {
            long expiry = Long.parseLong(payloadParts[1]);
            if (Instant.now().toEpochMilli() > expiry) {
                return null;
            }
        } catch (NumberFormatException ex) {
            return null;
        }
        return payloadParts[0];
    }

    private String hmac(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return encode(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo firmar el token", ex);
        }
    }

    private String encode(String value) {
        return encode(value.getBytes(StandardCharsets.UTF_8));
    }

    private String encode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String decode(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
