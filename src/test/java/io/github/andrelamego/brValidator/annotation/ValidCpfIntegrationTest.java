package io.github.andrelamego.brValidator.annotation;

import io.github.andrelamego.brValidator.cpf.CpfValidatorAutoConfiguration;
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
}
