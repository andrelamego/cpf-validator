package io.github.andrelamego.brValidator.service;

import io.github.andrelamego.brValidator.cnpj.CnpjValidationService;
import io.github.andrelamego.brValidator.cnpj.InvalidCnpjException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CnpjValidationServiceTests {

    private final CnpjValidationService service = new CnpjValidationService();

    @Test
    void deveValidarCnpjValido() {
        assertTrue(service.isValid("04.252.011/0001-10"));
        assertTrue(service.isValid("04252011000110"));
    }

    @Test
    void deveInvalidarCnpjComDigitosIguais() {
        assertFalse(service.isValid("11.111.111/1111-11"));
    }

    @Test
    void deveInvalidarCnpjComDigitoVerificadorInvalido() {
        assertFalse(service.isValid("04.252.011/0001-11"));
        assertFalse(service.isValid("04252011000111"));
    }

    @Test
    void deveInvalidarCnpjNuloOuVazio() {
        assertFalse(service.isValid(null));
        assertFalse(service.isValid(""));
        assertFalse(service.isValid("   "));
    }

    @Test
    void naoDeveAceitarFormatadoQuandoFormattedForFalse() {
        assertFalse(service.isValid("04.252.011/0001-10", false));
        assertTrue(service.isValid("04252011000110", false));
    }

    @Test
    void deveFormatarCnpjValido() {
        assertEquals("04.252.011/0001-10", service.formatar("04252011000110"));
    }

    @Test
    void deveLancarExcecaoAoFormatarCnpjInvalido() {
        assertThrows(InvalidCnpjException.class, () -> service.formatar("11111111111111"));
    }

    @Test
    void deveGerarCnpjValido() {
        String cnpj = service.gerarCnpjValido();
        assertTrue(service.isValid(cnpj));
        assertEquals(14, cnpj.length());
    }
}
