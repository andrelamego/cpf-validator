package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.validator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EmailValidator.class})
public @interface ValidEmail {
    String message() default "Email inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
    boolean allowPlusAlias() default true;
    boolean disposableAllowed() default true;
    String[] allowedDomains() default {};
    String[] blockedDomains() default {};
}
