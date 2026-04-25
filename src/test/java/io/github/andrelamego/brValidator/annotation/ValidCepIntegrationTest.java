package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.cep.CepValidatorAutoConfiguration;
import io.github.andrelamego.brValidator.cep.ValidCep;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidCepIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ValidationAutoConfiguration.class,
                    CepValidatorAutoConfiguration.class
            ));

    @Test
    void deveValidarCampoComAnnotation() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            CepDTO dtoValido = new CepDTO("01310-100");
            CepDTO dtoInvalido = new CepDTO("123");

            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoInvalido)).isNotEmpty();
        });
    }

    @Test
    void deveAceitarCampoVazioQuandoRequiredForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            CepOpcionalDTO dtoNulo = new CepOpcionalDTO(null);
            CepOpcionalDTO dtoVazio = new CepOpcionalDTO("   ");

            assertThat(validator.validate(dtoNulo)).isEmpty();
            assertThat(validator.validate(dtoVazio)).isEmpty();
        });
    }

    @Test
    void deveRespeitarParametroFormatted() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            CepSemMascaraDTO dtoComMascara = new CepSemMascaraDTO("01310-100");
            CepSemMascaraDTO dtoSemMascara = new CepSemMascaraDTO("01310100");

            assertThat(validator.validate(dtoComMascara)).isNotEmpty();
            assertThat(validator.validate(dtoSemMascara)).isEmpty();
        });
    }

    @Test
    void deveRespeitarParametroRejectRepeatedDigits() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            CepSemRepeticaoDTO dtoRepetido = new CepSemRepeticaoDTO("11111-111");
            CepSemRepeticaoDTO dtoNormal = new CepSemRepeticaoDTO("01310-100");

            assertThat(validator.validate(dtoRepetido)).isNotEmpty();
            assertThat(validator.validate(dtoNormal)).isEmpty();
        });
    }

    private static class CepDTO {
        @ValidCep
        private final String cep;

        private CepDTO(String cep) {
            this.cep = cep;
        }
    }

    private static class CepOpcionalDTO {
        @ValidCep(required = false)
        private final String cep;

        private CepOpcionalDTO(String cep) {
            this.cep = cep;
        }
    }

    private static class CepSemMascaraDTO {
        @ValidCep(formatted = false)
        private final String cep;

        private CepSemMascaraDTO(String cep) {
            this.cep = cep;
        }
    }

    private static class CepSemRepeticaoDTO {
        @ValidCep(rejectRepeatedDigits = true)
        private final String cep;

        private CepSemRepeticaoDTO(String cep) {
            this.cep = cep;
        }
    }
}
