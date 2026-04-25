package io.github.andrelamego.brValidator.birthdate;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BirthDateValidationServiceTests {

    private final BirthDateValidationService service = new BirthDateValidationService();

    @Test
    void deveValidarDataComConfiguracaoPadrao() {
        assertTrue(service.isValid(LocalDate.of(2000, 1, 1)));
    }

    @Test
    void deveInvalidarDataNula() {
        assertFalse(service.isValid(null));
    }

    @Test
    void deveRespeitarMinAgeEMaxAge() {
        LocalDate adulto = LocalDate.now().minusYears(30);
        LocalDate jovem = LocalDate.now().minusYears(10);

        assertTrue(service.isValid(adulto, 18, 65, false, true));
        assertFalse(service.isValid(jovem, 18, 65, false, true));
    }

    @Test
    void deveRespeitarAllowFutureDate() {
        LocalDate amanha = LocalDate.now().plusDays(1);

        assertFalse(service.isValid(amanha, 0, 120, false, true));
        assertTrue(service.isValid(amanha, 0, 120, true, true));
    }

    @Test
    void deveRespeitarAllowToday() {
        LocalDate hoje = LocalDate.now();

        assertTrue(service.isValid(hoje, 0, 120, false, true));
        assertFalse(service.isValid(hoje, 0, 120, false, false));
    }

    @Test
    void deveInvalidarQuandoMinAgeMaiorQueMaxAge() {
        assertFalse(service.isValid(LocalDate.of(2000, 1, 1), 80, 18, false, true));
    }
}
