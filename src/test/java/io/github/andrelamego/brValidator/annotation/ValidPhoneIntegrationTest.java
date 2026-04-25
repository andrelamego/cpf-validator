package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.phone.PhoneValidatorAutoConfiguration;
import io.github.andrelamego.brValidator.phone.ValidPhone;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidPhoneIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ValidationAutoConfiguration.class,
                    PhoneValidatorAutoConfiguration.class
            ));

    @Test
    void deveValidarCampoComAnnotation() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PhoneDTO dtoValido = new PhoneDTO("(11) 98765-4321");
            PhoneDTO dtoInvalido = new PhoneDTO("telefone-invalido");

            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoInvalido)).isNotEmpty();
        });
    }

    @Test
    void deveAceitarCampoVazioQuandoRequiredForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PhoneOpcionalDTO dtoNulo = new PhoneOpcionalDTO(null);
            PhoneOpcionalDTO dtoVazio = new PhoneOpcionalDTO("   ");

            assertThat(validator.validate(dtoNulo)).isEmpty();
            assertThat(validator.validate(dtoVazio)).isEmpty();
        });
    }

    @Test
    void deveRespeitarParametroFormatted() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PhoneSemMascaraDTO dtoComMascara = new PhoneSemMascaraDTO("(11) 98765-4321");
            PhoneSemMascaraDTO dtoSemMascara = new PhoneSemMascaraDTO("11987654321");

            assertThat(validator.validate(dtoComMascara)).isNotEmpty();
            assertThat(validator.validate(dtoSemMascara)).isEmpty();
        });
    }

    @Test
    void deveRespeitarAllowLandline() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PhoneSemFixoDTO dtoFixo = new PhoneSemFixoDTO("(11) 3234-5678");
            PhoneSemFixoDTO dtoCelular = new PhoneSemFixoDTO("(11) 98765-4321");

            assertThat(validator.validate(dtoFixo)).isNotEmpty();
            assertThat(validator.validate(dtoCelular)).isEmpty();
        });
    }

    @Test
    void deveRespeitarAllowCountryCode() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PhoneSemCountryCodeDTO dtoComCountryCode = new PhoneSemCountryCodeDTO("+55 (11) 98765-4321");
            PhoneSemCountryCodeDTO dtoSemCountryCode = new PhoneSemCountryCodeDTO("(11) 98765-4321");

            assertThat(validator.validate(dtoComCountryCode)).isNotEmpty();
            assertThat(validator.validate(dtoSemCountryCode)).isEmpty();
        });
    }

    @Test
    void deveRespeitarAllowedAreaCodesEBlockedAreaCodes() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PhoneAreaCodeRestritoDTO dtoPermitido = new PhoneAreaCodeRestritoDTO("(11) 98765-4321");
            PhoneAreaCodeRestritoDTO dtoNaoPermitido = new PhoneAreaCodeRestritoDTO("(21) 98765-4321");
            PhoneAreaCodeRestritoDTO dtoBloqueado = new PhoneAreaCodeRestritoDTO("(31) 98765-4321");

            assertThat(validator.validate(dtoPermitido)).isEmpty();
            assertThat(validator.validate(dtoNaoPermitido)).isNotEmpty();
            assertThat(validator.validate(dtoBloqueado)).isNotEmpty();
        });
    }

    @Test
    void deveRespeitarRejectRepeatedDigits() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PhoneComDigitosRepetidosDTO dtoRepetido = new PhoneComDigitosRepetidosDTO("11999999999");
            PhoneComDigitosRepetidosDTO dtoNormal = new PhoneComDigitosRepetidosDTO("11987654321");

            assertThat(validator.validate(dtoRepetido)).isNotEmpty();
            assertThat(validator.validate(dtoNormal)).isEmpty();
        });
    }

    private static class PhoneDTO {
        @ValidPhone
        private final String phone;

        private PhoneDTO(String phone) {
            this.phone = phone;
        }
    }

    private static class PhoneOpcionalDTO {
        @ValidPhone(required = false)
        private final String phone;

        private PhoneOpcionalDTO(String phone) {
            this.phone = phone;
        }
    }

    private static class PhoneSemMascaraDTO {
        @ValidPhone(formatted = false)
        private final String phone;

        private PhoneSemMascaraDTO(String phone) {
            this.phone = phone;
        }
    }

    private static class PhoneSemFixoDTO {
        @ValidPhone(allowLandline = false)
        private final String phone;

        private PhoneSemFixoDTO(String phone) {
            this.phone = phone;
        }
    }

    private static class PhoneSemCountryCodeDTO {
        @ValidPhone(allowCountryCode = false)
        private final String phone;

        private PhoneSemCountryCodeDTO(String phone) {
            this.phone = phone;
        }
    }

    private static class PhoneAreaCodeRestritoDTO {
        @ValidPhone(allowedAreaCodes = {"11", "31"}, blockedAreaCodes = {"31"})
        private final String phone;

        private PhoneAreaCodeRestritoDTO(String phone) {
            this.phone = phone;
        }
    }

    private static class PhoneComDigitosRepetidosDTO {
        @ValidPhone(rejectRepeatedDigits = true)
        private final String phone;

        private PhoneComDigitosRepetidosDTO(String phone) {
            this.phone = phone;
        }
    }
}
