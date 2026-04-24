package io.github.andrelamego.brValidator.password;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidationService {
    public boolean isValid(
            String password,
            int minLength,
            int maxLength,
            boolean requireUppercase,
            boolean requireLowercase,
            boolean requireNumber,
            boolean requireSpecialChar,
            boolean blockWhitespace
    ) {
        if (password == null || password.isBlank()) {
            return false;
        }

        if (password.length() < minLength || password.length() > maxLength) {
            return false;
        }

        if (blockWhitespace && password.matches(".*\\s.*")) {
            return false;
        }

        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            return false;
        }

        if (requireLowercase && !password.matches(".*[a-z].*")) {
            return false;
        }

        if (requireNumber && !password.matches(".*\\d.*")) {
            return false;
        }

        if (requireSpecialChar && !password.matches(".*[^A-Za-z0-9].*")) {
            return false;
        }

        return true;
    }

    public boolean isValid(String password) {
        return isValid(
                password,
                8,
                64,
                true,
                true,
                true,
                true,
                true
        );
    }
}
