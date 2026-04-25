package io.github.andrelamego.brValidator.cep;

import org.springframework.stereotype.Service;

@Service
public class CepValidationService {

    public boolean isValid(String cep, boolean acceptFormatted, boolean rejectRepeatedDigits) {
        if (cep == null || cep.isBlank()) {
            return false;
        }

        String normalized = cep.trim();

        if (!acceptFormatted && !normalized.matches("\\d{8}")) {
            return false;
        }

        normalized = normalized.replaceAll("[^0-9]", "");

        if (normalized.length() != 8) {
            return false;
        }

        if (rejectRepeatedDigits && normalized.matches("(\\d)\\1{7}")) {
            return false;
        }

        return true;
    }

    public boolean isValid(String cep) {
        return isValid(cep, true, true);
    }

    public String formatar(String cep) {
        if (!isValid(cep, true, true)) {
            throw new InvalidCepException("CEP inválido para formatação: " + cep);
        }

        String normalized = cep.replaceAll("[^0-9]", "");
        return normalized.substring(0, 5) + "-" + normalized.substring(5);
    }
}
