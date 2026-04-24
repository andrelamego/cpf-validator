package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.password.PasswordValidatorAutoConfiguration;
import io.github.andrelamego.brValidator.password.ValidPassword;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidPasswordIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ValidationAutoConfiguration.class,
                    PasswordValidatorAutoConfiguration.class
            ));

    @Test
    void deveValidarCampoComAnnotation() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PasswordDTO dtoValido = new PasswordDTO("Senha@123");
            PasswordDTO dtoInvalido = new PasswordDTO("senha");

            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoInvalido)).isNotEmpty();
        });
    }

    @Test
    void deveAceitarCampoVazioQuandoRequiredForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PasswordOpcionalDTO dtoNulo = new PasswordOpcionalDTO(null);
            PasswordOpcionalDTO dtoVazio = new PasswordOpcionalDTO("   ");

            assertThat(validator.validate(dtoNulo)).isEmpty();
            assertThat(validator.validate(dtoVazio)).isEmpty();
        });
    }

    @Test
    void deveRespeitarMinLengthEMaxLength() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PasswordTamanhoRestritoDTO dtoCurto = new PasswordTamanhoRestritoDTO("Aa1@");
            PasswordTamanhoRestritoDTO dtoValido = new PasswordTamanhoRestritoDTO("Aa1@abcd");
            PasswordTamanhoRestritoDTO dtoLongo = new PasswordTamanhoRestritoDTO("Aa1@abcdefghijkl");

            assertThat(validator.validate(dtoCurto)).isNotEmpty();
            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoLongo)).isNotEmpty();
        });
    }

    @Test
    void deveRespeitarRegraDeEspacosQuandoBlockWhitespaceForTrue() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PasswordSemEspacoDTO dtoComEspaco = new PasswordSemEspacoDTO("Senha @123");
            PasswordSemEspacoDTO dtoSemEspaco = new PasswordSemEspacoDTO("Senha@123");

            assertThat(validator.validate(dtoComEspaco)).isNotEmpty();
            assertThat(validator.validate(dtoSemEspaco)).isEmpty();
        });
    }

    @Test
    void devePermitirSenhaSemNumeroQuandoRequireNumberForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            PasswordSemNumeroObrigatorioDTO dtoSemNumero = new PasswordSemNumeroObrigatorioDTO("Senha@abc");
            PasswordSemNumeroObrigatorioDTO dtoInvalidoSemMaiuscula = new PasswordSemNumeroObrigatorioDTO("senha@abc");

            assertThat(validator.validate(dtoSemNumero)).isEmpty();
            assertThat(validator.validate(dtoInvalidoSemMaiuscula)).isNotEmpty();
        });
    }

    private static class PasswordDTO {
        @ValidPassword
        private final String password;

        private PasswordDTO(String password) {
            this.password = password;
        }
    }

    private static class PasswordOpcionalDTO {
        @ValidPassword(required = false)
        private final String password;

        private PasswordOpcionalDTO(String password) {
            this.password = password;
        }
    }

    private static class PasswordTamanhoRestritoDTO {
        @ValidPassword(minLength = 8, maxLength = 12)
        private final String password;

        private PasswordTamanhoRestritoDTO(String password) {
            this.password = password;
        }
    }

    private static class PasswordSemEspacoDTO {
        @ValidPassword(blockWhitespace = true)
        private final String password;

        private PasswordSemEspacoDTO(String password) {
            this.password = password;
        }
    }

    private static class PasswordSemNumeroObrigatorioDTO {
        @ValidPassword(requireNumber = false)
        private final String password;

        private PasswordSemNumeroObrigatorioDTO(String password) {
            this.password = password;
        }
    }
}
