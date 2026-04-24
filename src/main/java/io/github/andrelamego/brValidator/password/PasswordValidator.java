package io.github.andrelamego.brValidator.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Autowired
    private PasswordValidationService validationService;

    private boolean required;
    private int minLength;
    private int maxLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireNumber;
    private boolean requireSpecialChar;
    private boolean blockWhitespace;

    @Override
    public void initialize(ValidPassword annotation) {
        this.required = annotation.required();
        this.minLength = annotation.minLength();
        this.maxLength = annotation.maxLength();
        this.requireUppercase = annotation.requireUppercase();
        this.requireLowercase = annotation.requireLowercase();
        this.requireNumber = annotation.requireNumber();
        this.requireSpecialChar = annotation.requireSpecialChar();
        this.blockWhitespace = annotation.blockWhitespace();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!required && (value == null || value.isBlank())) {
            return true;
        }

        if (required && (value == null || value.isBlank())) {
            return false;
        }

        return validationService.isValid(
                value,
                minLength,
                maxLength,
                requireUppercase,
                requireLowercase,
                requireNumber,
                requireSpecialChar,
                blockWhitespace
        );
    }
}
