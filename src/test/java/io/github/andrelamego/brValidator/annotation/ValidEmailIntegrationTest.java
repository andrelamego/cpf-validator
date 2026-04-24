package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.email.EmailValidatorAutoConfiguration;
import io.github.andrelamego.brValidator.email.ValidEmail;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidEmailIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ValidationAutoConfiguration.class,
                    EmailValidatorAutoConfiguration.class
            ));

    @Test
    void deveValidarCampoComAnnotation() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmailDTO dtoValido = new EmailDTO("usuario@email.com");
            EmailDTO dtoInvalido = new EmailDTO("usuarioemail.com");

            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoInvalido)).isNotEmpty();
        });
    }

    @Test
    void naoDeveAceitarPlusAliasQuandoAllowPlusAliasForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmailSemAliasDTO dtoComAlias = new EmailSemAliasDTO("usuario+tag@email.com");
            EmailSemAliasDTO dtoSemAlias = new EmailSemAliasDTO("usuario@email.com");

            assertThat(validator.validate(dtoComAlias)).isNotEmpty();
            assertThat(validator.validate(dtoSemAlias)).isEmpty();
        });
    }

    @Test
    void deveAceitarCampoVazioQuandoRequiredForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmailOpcionalDTO dtoNulo = new EmailOpcionalDTO(null);
            EmailOpcionalDTO dtoVazio = new EmailOpcionalDTO("   ");

            assertThat(validator.validate(dtoNulo)).isEmpty();
            assertThat(validator.validate(dtoVazio)).isEmpty();
        });
    }

    @Test
    void deveRespeitarDominiosPermitidosEBloqueados() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmailDominioRestritoDTO dtoPermitido = new EmailDominioRestritoDTO("usuario@empresa.com");
            EmailDominioRestritoDTO dtoNaoPermitido = new EmailDominioRestritoDTO("usuario@email.com");
            EmailDominioRestritoDTO dtoBloqueado = new EmailDominioRestritoDTO("usuario@bloqueado.com");

            assertThat(validator.validate(dtoPermitido)).isEmpty();
            assertThat(validator.validate(dtoNaoPermitido)).isNotEmpty();
            assertThat(validator.validate(dtoBloqueado)).isNotEmpty();
        });
    }

    @Test
    void naoDeveAceitarDominioDescartavelQuandoDisposableAllowedForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            EmailSemDescartavelDTO dtoDescartavel = new EmailSemDescartavelDTO("usuario@mailinator.com");
            EmailSemDescartavelDTO dtoComum = new EmailSemDescartavelDTO("usuario@empresa.com");

            assertThat(validator.validate(dtoDescartavel)).isNotEmpty();
            assertThat(validator.validate(dtoComum)).isEmpty();
        });
    }

    private static class EmailDTO {
        @ValidEmail
        private final String email;

        private EmailDTO(String email) {
            this.email = email;
        }
    }

    private static class EmailSemAliasDTO {
        @ValidEmail(allowPlusAlias = false)
        private final String email;

        private EmailSemAliasDTO(String email) {
            this.email = email;
        }
    }

    private static class EmailOpcionalDTO {
        @ValidEmail(required = false)
        private final String email;

        private EmailOpcionalDTO(String email) {
            this.email = email;
        }
    }

    private static class EmailDominioRestritoDTO {
        @ValidEmail(allowedDomains = {"empresa.com"}, blockedDomains = {"bloqueado.com"})
        private final String email;

        private EmailDominioRestritoDTO(String email) {
            this.email = email;
        }
    }

    private static class EmailSemDescartavelDTO {
        @ValidEmail(disposableAllowed = false)
        private final String email;

        private EmailSemDescartavelDTO(String email) {
            this.email = email;
        }
    }
}
