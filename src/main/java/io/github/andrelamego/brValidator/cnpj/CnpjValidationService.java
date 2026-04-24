package io.github.andrelamego.brValidator.cnpj;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class CnpjValidationService {
    public boolean isValid(String cnpj, boolean acceptFormatted) {
        if (cnpj == null || cnpj.isBlank()) {
            return false;
        }

        String cnpjLimpo = cnpj;

        if (acceptFormatted) {
            cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        }

        if (cnpjLimpo.length() != 14) {
            return false;
        }

        if (cnpjLimpo.matches("(\\d)\\1{13}")) {
            return false;
        }

        return validarDigitos(cnpjLimpo);
    }

    public boolean isValid(String cnpj) {
        return isValid(cnpj, true);
    }

    private boolean validarDigitos(String cnpjLimpo) {
        try {
            int digito1 = calcularDigito(cnpjLimpo.substring(0, 12), 5);
            int digito2 = calcularDigito(cnpjLimpo.substring(0, 12) + digito1, 6);

            int digitoInformado1 = Character.getNumericValue(cnpjLimpo.charAt(12));
            int digitoInformado2 = Character.getNumericValue(cnpjLimpo.charAt(13));

            return digito1 == digitoInformado1 && digito2 == digitoInformado2;
        } catch (Exception e) {
            return false;
        }
    }

    private int calcularDigito(String cnpj, int peso) {
        int soma = 0;
        int pesoAtual = peso;

        for (int i = 0; i < cnpj.length(); i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesoAtual;
            pesoAtual--;
            if (pesoAtual < 2) {
                pesoAtual = 9;
            }
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    public String formatar(String cnpj) {
        if (!isValid(cnpj)) {
            throw new InvalidCnpjException("CNPJ invalido para formatacao: " + cnpj);
        }

        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");

        return String.format("%s.%s.%s/%s-%s",
                cnpjLimpo.substring(0, 2),
                cnpjLimpo.substring(2, 5),
                cnpjLimpo.substring(5, 8),
                cnpjLimpo.substring(8, 12),
                cnpjLimpo.substring(12, 14)
        );
    }

    public String gerarCnpjValido() {
        int[] numeros = new int[12];

        for (int i = 0; i < 12; i++) {
            numeros[i] = ThreadLocalRandom.current().nextInt(10);
        }

        int digito1 = calcularDigitoArray(numeros, 5);
        int digito2 = calcularDigitoArray(adicionarDigito(numeros, digito1), 6);

        StringBuilder cnpj = new StringBuilder();
        for (int num : numeros) {
            cnpj.append(num);
        }

        cnpj.append(digito1).append(digito2);

        return cnpj.toString();
    }

    private int calcularDigitoArray(int[] numeros, int peso) {
        int soma = 0;
        int pesoAtual = peso;

        for (int i = 0; i < numeros.length; i++) {
            soma += numeros[i] * pesoAtual;
            pesoAtual--;
            if (pesoAtual < 2) {
                pesoAtual = 9;
            }
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private int[] adicionarDigito(int[] array, int digito) {
        int[] novo = new int[array.length + 1];
        System.arraycopy(array, 0, novo, 0, array.length);
        novo[array.length] = digito;
        return novo;
    }
}
