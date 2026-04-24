package io.github.andrelamego.brValidator.cnpj;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CnpjValidator implements ConstraintValidator<ValidCnpj, String> {

    @Autowired
    private CnpjValidationService validationService;

    private boolean formatted;
    private boolean required;

    @Override
    public void initialize(ValidCnpj annotation) {
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

        //Valida CNPJ
        return validationService.isValid(value, formatted);
    }
}
