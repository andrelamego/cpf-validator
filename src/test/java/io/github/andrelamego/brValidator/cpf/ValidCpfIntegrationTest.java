package io.github.andrelamego.brValidator.cpf;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidCpfIntegrationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ValidationAutoConfiguration.class,
                    CpfValidatorAutoConfiguration.class
            ));

    @Test
    void deveValidarCampoComAnnotation() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PessoaDTO dtoValido = new PessoaDTO("529.982.247-25");
            PessoaDTO dtoInvalido = new PessoaDTO("111.111.111-11");

            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoInvalido)).isNotEmpty();
        });
    }

    @Test
    void naoDeveAceitarFormatadoQuandoFormattedForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PessoaSemMascaraDTO dtoFormatado = new PessoaSemMascaraDTO("529.982.247-25");
            PessoaSemMascaraDTO dtoSemMascara = new PessoaSemMascaraDTO("52998224725");

            assertThat(validator.validate(dtoFormatado)).isNotEmpty();
            assertThat(validator.validate(dtoSemMascara)).isEmpty();
        });
    }

    @Test
    void deveAceitarCampoVazioQuandoRequiredForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PessoaOpcionalDTO dtoNulo = new PessoaOpcionalDTO(null);
            PessoaOpcionalDTO dtoVazio = new PessoaOpcionalDTO("   ");

            assertThat(validator.validate(dtoNulo)).isEmpty();
            assertThat(validator.validate(dtoVazio)).isEmpty();
        });
    }

    private static class PessoaDTO {
        @ValidCpf
        private final String cpf;

        private PessoaDTO(String cpf) {
            this.cpf = cpf;
        }
    }

    private static class PessoaSemMascaraDTO {
        @ValidCpf(formatted = false)
        private final String cpf;

        private PessoaSemMascaraDTO(String cpf) {
            this.cpf = cpf;
        }
    }

    private static class PessoaOpcionalDTO {
        @ValidCpf(required = false)
        private final String cpf;

        private PessoaOpcionalDTO(String cpf) {
            this.cpf = cpf;
        }
    }
}
