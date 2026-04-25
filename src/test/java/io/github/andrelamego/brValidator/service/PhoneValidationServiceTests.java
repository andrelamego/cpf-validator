package io.github.andrelamego.brValidator.service;

import io.github.andrelamego.brValidator.phone.PhoneValidationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneValidationServiceTests {

    private final PhoneValidationService service = new PhoneValidationService();

    @Test
    void deveValidarTelefoneCelularComConfiguracaoPadrao() {
        assertTrue(service.isValid("11987654321"));
        assertTrue(service.isValid("(11) 98765-4321"));
        assertTrue(service.isValid("+55 (11) 98765-4321"));
    }

    @Test
    void deveInvalidarTelefoneNuloOuVazio() {
        assertFalse(service.isValid(null));
        assertFalse(service.isValid(""));
        assertFalse(service.isValid("   "));
    }

    @Test
    void deveInvalidarTelefoneFixoQuandoAllowLandlineForFalse() {
        assertFalse(service.isValid(
                "(11) 3234-5678",
                true,
                false,
                true,
                true,
                new String[]{},
                new String[]{}
        ));
    }

    @Test
    void deveAceitarTelefoneFixoQuandoAllowLandlineForTrue() {
        assertTrue(service.isValid(
                "(11) 3234-5678",
                true,
                true,
                true,
                true,
                new String[]{},
                new String[]{}
        ));
    }

    @Test
    void deveRespeitarAllowCountryCode() {
        assertFalse(service.isValid(
                "+55 (11) 98765-4321",
                true,
                false,
                false,
                true,
                new String[]{},
                new String[]{}
        ));
    }

    @Test
    void deveRespeitarAllowedAreaCodesEBlockedAreaCodes() {
        assertTrue(service.isValid(
                "11987654321",
                true,
                false,
                true,
                true,
                new String[]{"11"},
                new String[]{}
        ));

        assertFalse(service.isValid(
                "21987654321",
                true,
                false,
                true,
                true,
                new String[]{"11"},
                new String[]{}
        ));

        assertFalse(service.isValid(
                "11987654321",
                true,
                false,
                true,
                true,
                new String[]{},
                new String[]{"11"}
        ));
    }

    @Test
    void deveRespeitarRejectRepeatedDigits() {
        assertFalse(service.isValid(
                "11999999999",
                true,
                false,
                true,
                true,
                new String[]{},
                new String[]{}
        ));

        assertTrue(service.isValid(
                "11999999999",
                true,
                false,
                true,
                false,
                new String[]{},
                new String[]{}
        ));
    }

    @Test
    void deveRespeitarParametroFormatted() {
        assertTrue(service.isValid(
                "11987654321",
                false,
                false,
                true,
                true,
                new String[]{},
                new String[]{}
        ));

        assertFalse(service.isValid(
                "(11) 98765-4321",
                false,
                false,
                true,
                true,
                new String[]{},
                new String[]{}
        ));
    }
}
