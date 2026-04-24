package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.cnpj.ValidCnpj;
import io.github.andrelamego.brValidator.cnpj.CnpjValidatorAutoConfiguration;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidCnpjIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ValidationAutoConfiguration.class,
                    CnpjValidatorAutoConfiguration.class
            ));

    @Test
    void deveValidarCampoComAnnotation() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmpresaDTO dtoValido = new EmpresaDTO("04.252.011/0001-10");
            EmpresaDTO dtoInvalido = new EmpresaDTO("11.111.111/1111-11");

            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoInvalido)).isNotEmpty();
        });
    }

    @Test
    void naoDeveAceitarFormatadoQuandoFormattedForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmpresaSemMascaraDTO dtoFormatado = new EmpresaSemMascaraDTO("04.252.011/0001-10");
            EmpresaSemMascaraDTO dtoSemMascara = new EmpresaSemMascaraDTO("04252011000110");

            assertThat(validator.validate(dtoFormatado)).isNotEmpty();
            assertThat(validator.validate(dtoSemMascara)).isEmpty();
        });
    }

    @Test
    void deveAceitarCampoVazioQuandoRequiredForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmpresaOpcionalDTO dtoNulo = new EmpresaOpcionalDTO(null);
            EmpresaOpcionalDTO dtoVazio = new EmpresaOpcionalDTO("   ");

            assertThat(validator.validate(dtoNulo)).isEmpty();
            assertThat(validator.validate(dtoVazio)).isEmpty();
        });
    }

    private static class EmpresaDTO {
        @ValidCnpj(formatted = true)
        private final String cnpj;

        private EmpresaDTO(String cnpj) {
            this.cnpj = cnpj;
        }
    }

    private static class EmpresaSemMascaraDTO {
        @ValidCnpj(formatted = false)
        private final String cnpj;

        private EmpresaSemMascaraDTO(String cnpj) {
            this.cnpj = cnpj;
        }
    }

    private static class EmpresaOpcionalDTO {
        @ValidCnpj(required = false)
        private final String cnpj;

        private EmpresaOpcionalDTO(String cnpj) {
            this.cnpj = cnpj;
        }
    }
}
