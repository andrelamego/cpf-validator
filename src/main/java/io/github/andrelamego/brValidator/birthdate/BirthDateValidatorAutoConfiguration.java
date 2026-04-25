package io.github.andrelamego.brValidator.birthdate;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class BirthDateValidatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BirthDateValidationService birthDateValidationService() {
        return new BirthDateValidationService();
    }
}
