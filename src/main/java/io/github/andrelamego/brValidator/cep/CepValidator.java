package io.github.andrelamego.brValidator.cep;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CepValidator implements ConstraintValidator<ValidCep, String> {
    @Autowired
    private CepValidationService validationService;

    private boolean required;
    private boolean formatted;
    private boolean rejectRepeatedDigits;

    @Override
    public void initialize(ValidCep annotation) {
        this.required = annotation.required();
        this.formatted = annotation.formatted();
        this.rejectRepeatedDigits = annotation.rejectRepeatedDigits();
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
                formatted,
                rejectRepeatedDigits
        );
    }
}
