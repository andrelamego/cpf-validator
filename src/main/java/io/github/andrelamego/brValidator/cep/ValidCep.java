package io.github.andrelamego.brValidator.cep;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CepValidator.class})
public @interface ValidCep {
    String message() default "CEP inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
    boolean formatted() default true;
    boolean rejectRepeatedDigits() default true;
}
