package io.github.andrelamego.brValidator.cnpj;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CnpjValidator.class})
public @interface ValidCnpj {
    String message() default "CNPJ inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean formatted() default true;
    boolean required() default true;
}
