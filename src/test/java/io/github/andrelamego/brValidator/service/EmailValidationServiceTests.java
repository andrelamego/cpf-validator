package io.github.andrelamego.brValidator.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidationServiceTests {

    private final EmailValidationService service = new EmailValidationService();

    @Test
    void deveValidarEmailBasico() {
        assertTrue(service.isValid("usuario@email.com"));
        assertTrue(service.isValid(" usuario@email.com "));
    }

    @Test
    void deveInvalidarEmailNuloOuVazio() {
        assertFalse(service.isValid(null));
        assertFalse(service.isValid(""));
        assertFalse(service.isValid("   "));
    }

    @Test
    void deveInvalidarEmailComFormatoInvalido() {
        assertFalse(service.isValid("usuarioemail.com"));
        assertFalse(service.isValid("usuario@@email.com"));
    }

    @Test
    void naoDeveAceitarPlusAliasQuandoAllowPlusAliasForFalse() {
        assertFalse(service.isValid(
                "usuario+tag@email.com",
                false,
                true,
                new String[]{},
                new String[]{}
        ));
    }

    @Test
    void naoDeveAceitarDominioDescartavelQuandoDisposableAllowedForFalse() {
        assertFalse(service.isValid(
                "usuario@mailinator.com",
                true,
                false,
                new String[]{},
                new String[]{}
        ));
    }

    @Test
    void deveRespeitarListaDeDominiosPermitidos() {
        assertTrue(service.isValid(
                "usuario@empresa.com",
                true,
                true,
                new String[]{"empresa.com"},
                new String[]{}
        ));
        assertFalse(service.isValid(
                "usuario@email.com",
                true,
                true,
                new String[]{"empresa.com"},
                new String[]{}
        ));
    }

    @Test
    void deveRespeitarListaDeDominiosBloqueados() {
        assertFalse(service.isValid(
                "usuario@bloqueado.com",
                true,
                true,
                new String[]{},
                new String[]{"bloqueado.com"}
        ));
    }
}
