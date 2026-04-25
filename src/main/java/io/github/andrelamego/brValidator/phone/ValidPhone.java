package io.github.andrelamego.brValidator.phone;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PhoneValidator.class})
public @interface ValidPhone {
    String message() default "Telefone inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
    boolean formatted() default true;
    boolean allowLandline() default false;
    boolean allowCountryCode() default true;
    boolean rejectRepeatedDigits() default true;
    String[] allowedAreaCodes() default {};
    String[] blockedAreaCodes() default {};
}
