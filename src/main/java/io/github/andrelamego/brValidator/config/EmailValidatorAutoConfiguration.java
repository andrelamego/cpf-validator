package io.github.andrelamego.brValidator.config;

import io.github.andrelamego.brValidator.service.EmailValidationService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class EmailValidatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public EmailValidationService emailValidationService() {
        return new EmailValidationService();
    }
}
