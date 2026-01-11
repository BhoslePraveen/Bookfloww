package org.praveenit.bookfloww.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class HashService {
    @Value("${security.hash.bcrypt.strength:12}")
    private int bcryptStrength;

    /**
     * Hash a raw value using BCrypt
     */
    public String hash(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Cannot hash null or blank value");
        }
        return BCrypt.hashpw(raw, BCrypt.gensalt(bcryptStrength));
    }

    /**
     * Verify raw value against BCrypt hash
     */
    public boolean matches(String raw, String hash) {
        if (raw == null || hash == null) {
            return false;
        }
        return BCrypt.checkpw(raw, hash);
    }
}
