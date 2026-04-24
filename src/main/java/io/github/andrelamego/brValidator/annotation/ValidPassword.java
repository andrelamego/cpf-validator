package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface ValidPassword {
    String message() default "Senha inválida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
    int minLength() default 8;
    int maxLength() default 64;
    boolean requireUppercase() default true;
    boolean requireLowercase() default true;
    boolean requireNumber() default true;
    boolean requireSpecialChar() default true;
    boolean blockWhitespace() default true;
}
