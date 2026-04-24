package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.validator.CpfValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CpfValidator.class)
public @interface ValidCpf {
    String message() default "CPF inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean formatted() default true;
    boolean required() default true;
}
