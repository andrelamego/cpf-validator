package io.github.andrelamego.brValidator.password;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class PasswordValidatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PasswordValidationService passwordValidationService() {
        return new PasswordValidationService();
    }
}
