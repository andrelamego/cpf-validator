package io.github.andrelamego.brValidator.birthdate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BirthDateValidator.class})
public @interface ValidBirthDate {
    String message() default "Data de Nascimento inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
    int minAge() default 0;
    int maxAge() default 120;
    boolean allowFutureDate() default false;
    boolean allowToday() default true;
}
