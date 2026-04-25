package io.github.andrelamego.brValidator.birthdate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Autowired
    private BirthDateValidationService validationService;

    private boolean required;
    private int minAge;
    private int maxAge;
    private boolean allowFutureDate;
    private boolean allowToday;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (!required && value == null) {
            return true;
        }

        if (required && value == null) {
            return false;
        }

        return validationService.isValid(
                value,
                minAge,
                maxAge,
                allowFutureDate,
                allowToday
        );
    }

    @Override
    public void initialize(ValidBirthDate annotation) {
        this.required = annotation.required();
        this.minAge = annotation.minAge();
        this.maxAge = annotation.maxAge();
        this.allowFutureDate = annotation.allowFutureDate();
        this.allowToday = annotation.allowToday();
    }
}
