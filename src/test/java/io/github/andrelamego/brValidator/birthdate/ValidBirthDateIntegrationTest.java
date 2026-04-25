package io.github.andrelamego.brValidator.birthdate;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidBirthDateIntegrationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ValidationAutoConfiguration.class,
                    BirthDateValidatorAutoConfiguration.class
            ));

    @Test
    void deveValidarCampoComAnnotation() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            BirthDateDTO dtoValido = new BirthDateDTO(LocalDate.of(2000, 1, 1));
            BirthDateDTO dtoInvalido = new BirthDateDTO(LocalDate.now().plusDays(1));

            assertThat(validator.validate(dtoValido)).isEmpty();
            assertThat(validator.validate(dtoInvalido)).isNotEmpty();
        });
    }

    @Test
    void deveAceitarCampoVazioQuandoRequiredForFalse() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            BirthDateOpcionalDTO dtoNulo = new BirthDateOpcionalDTO(null);

            assertThat(validator.validate(dtoNulo)).isEmpty();
        });
    }

    @Test
    void deveRespeitarMinAgeEMaxAge() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            LocalDate menor = LocalDate.now().minusYears(10);
            LocalDate adulto = LocalDate.now().minusYears(30);

            BirthDateComFaixaDTO dtoMenor = new BirthDateComFaixaDTO(menor);
            BirthDateComFaixaDTO dtoAdulto = new BirthDateComFaixaDTO(adulto);

            assertThat(validator.validate(dtoMenor)).isNotEmpty();
            assertThat(validator.validate(dtoAdulto)).isEmpty();
        });
    }

    @Test
    void deveRespeitarAllowFutureDateEAllowToday() {
        contextRunner.run(context -> {
            Validator validator = context.getBean(Validator.class);

            LocalDate amanha = LocalDate.now().plusDays(1);
            LocalDate hoje = LocalDate.now();

            BirthDateSemFuturoDTO dtoFuturo = new BirthDateSemFuturoDTO(amanha);
            BirthDateSemHojeDTO dtoHoje = new BirthDateSemHojeDTO(hoje);
            BirthDateComFuturoDTO dtoFuturoPermitido = new BirthDateComFuturoDTO(amanha);

            assertThat(validator.validate(dtoFuturo)).isNotEmpty();
            assertThat(validator.validate(dtoHoje)).isNotEmpty();
            assertThat(validator.validate(dtoFuturoPermitido)).isEmpty();
        });
    }

    private static class BirthDateDTO {
        @ValidBirthDate
        private final LocalDate birthDate;

        private BirthDateDTO(LocalDate birthDate) {
            this.birthDate = birthDate;
        }
    }

    private static class BirthDateOpcionalDTO {
        @ValidBirthDate(required = false)
        private final LocalDate birthDate;

        private BirthDateOpcionalDTO(LocalDate birthDate) {
            this.birthDate = birthDate;
        }
    }

    private static class BirthDateComFaixaDTO {
        @ValidBirthDate(minAge = 18, maxAge = 65)
        private final LocalDate birthDate;

        private BirthDateComFaixaDTO(LocalDate birthDate) {
            this.birthDate = birthDate;
        }
    }

    private static class BirthDateSemFuturoDTO {
        @ValidBirthDate(allowFutureDate = false)
        private final LocalDate birthDate;

        private BirthDateSemFuturoDTO(LocalDate birthDate) {
            this.birthDate = birthDate;
        }
    }

    private static class BirthDateSemHojeDTO {
        @ValidBirthDate(allowToday = false)
        private final LocalDate birthDate;

        private BirthDateSemHojeDTO(LocalDate birthDate) {
            this.birthDate = birthDate;
        }
    }

    private static class BirthDateComFuturoDTO {
        @ValidBirthDate(allowFutureDate = true)
        private final LocalDate birthDate;

        private BirthDateComFuturoDTO(LocalDate birthDate) {
            this.birthDate = birthDate;
        }
    }
}
