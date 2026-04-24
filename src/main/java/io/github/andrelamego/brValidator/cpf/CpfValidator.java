package io.github.andrelamego.brValidator.cpf;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Autowired
    private CpfValidationService validationService;

    private boolean formatted;
    private boolean required;

    @Override
    public void initialize(ValidCpf annotation) {
        this.formatted = annotation.formatted();
        this.required = annotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Campo opcional e vazio
        if (!required && (value == null || value.isBlank())) {
            return true;
        }

        // Campo obrigatorio e vazio
        if (required && (value == null || value.isBlank())) {
            return false;
        }

        // Valida CPF
        return validationService.isValid(value, formatted);
    }
}
