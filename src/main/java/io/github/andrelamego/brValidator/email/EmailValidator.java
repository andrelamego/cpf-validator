package io.github.andrelamego.brValidator.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Autowired
    private EmailValidationService validationService;

    private boolean required;
    private boolean allowPlusAlias;
    private boolean disposableAllowed;
    private String[] allowedDomains;
    private String[] blockedDomains;

    @Override
    public void initialize(ValidEmail annotation) {
        this.required = annotation.required();
        this.allowPlusAlias = annotation.allowPlusAlias();
        this.disposableAllowed = annotation.disposableAllowed();
        this.allowedDomains = annotation.allowedDomains();
        this.blockedDomains = annotation.blockedDomains();
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
                allowPlusAlias,
                disposableAllowed,
                allowedDomains,
                blockedDomains
        );
    }
}
