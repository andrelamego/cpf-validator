package io.github.andrelamego.brValidator.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Autowired
    private PhoneValidationService validationService;

    private boolean required;
    private boolean formatted;
    private boolean allowLandline;
    private boolean allowCountryCode;
    private boolean rejectRepeatedDigits;
    private String[] allowedAreaCodes;
    private String[] blockedAreaCodes;

    @Override
    public void initialize(ValidPhone annotation) {
        this.required = annotation.required();
        this.formatted = annotation.formatted();
        this.allowLandline = annotation.allowLandline();
        this.allowCountryCode = annotation.allowCountryCode();
        this.rejectRepeatedDigits = annotation.rejectRepeatedDigits();
        this.allowedAreaCodes = annotation.allowedAreaCodes();
        this.blockedAreaCodes = annotation.blockedAreaCodes();
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
                allowLandline,
                allowCountryCode,
                rejectRepeatedDigits,
                allowedAreaCodes,
                blockedAreaCodes
        );
    }
}
