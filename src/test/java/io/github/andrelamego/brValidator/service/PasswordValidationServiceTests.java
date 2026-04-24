package io.github.andrelamego.brValidator.service;

import io.github.andrelamego.brValidator.password.PasswordValidationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidationServiceTests {

    private final PasswordValidationService service = new PasswordValidationService();

    @Test
    void deveValidarSenhaComConfiguracaoPadrao() {
        assertTrue(service.isValid("Senha@123"));
    }

    @Test
    void deveInvalidarSenhaNulaOuVazia() {
        assertFalse(service.isValid(null));
        assertFalse(service.isValid(""));
        assertFalse(service.isValid("   "));
    }

    @Test
    void deveInvalidarSenhaMenorQueMinLength() {
        assertFalse(service.isValid(
                "Aa1@abc",
                8,
                64,
                true,
                true,
                true,
                true,
                true
        ));
    }

    @Test
    void deveInvalidarSenhaMaiorQueMaxLength() {
        assertFalse(service.isValid(
                "Aa1@" + "x".repeat(30),
                8,
                16,
                true,
                true,
                true,
                true,
                true
        ));
    }

    @Test
    void deveRespeitarRegraDeMaiuscula() {
        assertFalse(service.isValid(
                "senha@123",
                8,
                64,
                true,
                true,
                true,
                true,
                true
        ));
    }

    @Test
    void deveRespeitarRegraDeMinuscula() {
        assertFalse(service.isValid(
                "SENHA@123",
                8,
                64,
                true,
                true,
                true,
                true,
                true
        ));
    }

    @Test
    void deveRespeitarRegraDeNumero() {
        assertFalse(service.isValid(
                "Senha@abc",
                8,
                64,
                true,
                true,
                true,
                true,
                true
        ));
    }

    @Test
    void deveRespeitarRegraDeCaractereEspecial() {
        assertFalse(service.isValid(
                "Senha1234",
                8,
                64,
                true,
                true,
                true,
                true,
                true
        ));
    }

    @Test
    void deveBloquearEspacosQuandoBlockWhitespaceForTrue() {
        assertFalse(service.isValid(
                "Senha @123",
                8,
                64,
                true,
                true,
                true,
                true,
                true
        ));
    }

    @Test
    void devePermitirSenhaMaisSimplesQuandoRegrasDesativadas() {
        assertTrue(service.isValid(
                "somentesenha",
                4,
                64,
                false,
                false,
                false,
                false,
                false
        ));
    }
}
