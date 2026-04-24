package io.github.andrelamego.brValidator.cpf;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class CpfValidationService {

    public boolean isValid(String cpf, boolean acceptFormatted) {
        if (cpf == null || cpf.isBlank()) {
            return false;
        }

        String cpfLimpo = cpf;

        if (acceptFormatted) {
            cpfLimpo = cpf.replaceAll("[^0-9]", "");
        }

        if (cpfLimpo.length() != 11) {
            return false;
        }

        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            return false;
        }

        return validarDigitos(cpfLimpo);
    }

    public boolean isValid(String cpf) {
        return isValid(cpf, true);
    }

    private boolean validarDigitos(String cpf) {
        try {
            int digito1 = calcularDigito(cpf, 10);
            int digito2 = calcularDigito(cpf, 11);

            int digitoInformado1 = Character.getNumericValue(cpf.charAt(9));
            int digitoInformado2 = Character.getNumericValue(cpf.charAt(10));

            return digito1 == digitoInformado1 && digito2 == digitoInformado2;
        } catch (Exception e) {
            return false;
        }
    }

    private int calcularDigito(String cpf, int peso) {
        int soma = 0;

        for (int i = 0; i < peso - 1; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (peso - i);
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    public String formatar(String cpf) {
        if (!isValid(cpf)) {
            throw new InvalidCpfException("CPF inválido para formatação: " + cpf);
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        return String.format("%s.%s.%s-%s",
                cpfLimpo.substring(0, 3),
                cpfLimpo.substring(3, 6),
                cpfLimpo.substring(6, 9),
                cpfLimpo.substring(9, 11)
        );
    }

    public String gerarCpfValido() {
        int[] numeros = new int[9];

        for (int i = 0; i < 9; i++) {
            numeros[i] = ThreadLocalRandom.current().nextInt(10);
        }

        int digito1 = calcularDigitoArray(numeros, 10);
        int digito2 = calcularDigitoArray(adicionarDigito(numeros, digito1), 11);

        StringBuilder cpf = new StringBuilder();
        for (int num : numeros) {
            cpf.append(num);
        }

        cpf.append(digito1).append(digito2);

        return cpf.toString();
    }

    private int calcularDigitoArray(int[] numeros, int peso) {
        int soma = 0;

        for (int i = 0; i < numeros.length; i++) {
            soma += numeros[i] * (peso - i);
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
