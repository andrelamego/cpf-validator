package io.github.andrelamego.brValidator.service;

import io.github.andrelamego.brValidator.cpf.CpfValidationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfValidationServiceTests {

    private final CpfValidationService service = new CpfValidationService();

    @Test
    void deveValidarCpfValido() {
        assertTrue(service.isValid("529.982.247-25"));
        assertTrue(service.isValid("52998224725"));
    }

    @Test
    void deveInvalidarCpfComDigitosIguais() {
        assertFalse(service.isValid("111.111.111-11"));
    }

    @Test
    void deveInvalidarCpfNuloOuVazio() {
        assertFalse(service.isValid(null));
        assertFalse(service.isValid(""));
        assertFalse(service.isValid("   "));
    }

    @Test
    void naoDeveAceitarFormatadoQuandoFormattedForFalse() {
        assertFalse(service.isValid("529.982.247-25", false));
        assertTrue(service.isValid("52998224725", false));
    }

    @Test
    void deveFormatarCpfValido() {
        assertEquals("529.982.247-25", service.formatar("52998224725"));
    }

    @Test
    void deveGerarCpfValido() {
        String cpf = service.gerarCpfValido();
        assertTrue(service.isValid(cpf));
    }
}
