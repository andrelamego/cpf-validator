package io.github.andrelamego.brValidator.service;

import io.github.andrelamego.brValidator.cep.CepValidationService;
import io.github.andrelamego.brValidator.cep.InvalidCepException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CepValidationServiceTests {

    private final CepValidationService service = new CepValidationService();

    @Test
    void deveValidarCepComConfiguracaoPadrao() {
        assertTrue(service.isValid("01310-100"));
        assertTrue(service.isValid("01310100"));
    }

    @Test
    void deveInvalidarCepNuloOuVazio() {
        assertFalse(service.isValid(null));
        assertFalse(service.isValid(""));
        assertFalse(service.isValid("   "));
    }

    @Test
    void deveRespeitarParametroAcceptFormatted() {
        assertTrue(service.isValid("01310100", false, true));
        assertFalse(service.isValid("01310-100", false, true));
    }

    @Test
    void deveInvalidarCepComTamanhoDiferenteDeOito() {
        assertFalse(service.isValid("01310-10"));
        assertFalse(service.isValid("01310-1000"));
    }

    @Test
    void deveRespeitarParametroRejectRepeatedDigits() {
        assertFalse(service.isValid("11111-111", true, true));
        assertTrue(service.isValid("11111-111", true, false));
    }

    @Test
    void deveFormatarCepValido() {
        assertEquals("01310-100", service.formatar("01310100"));
        assertEquals("01310-100", service.formatar("01310-100"));
    }

    @Test
    void deveLancarExcecaoAoFormatarCepInvalido() {
        assertThrows(InvalidCepException.class, () -> service.formatar("123"));
    }
}
