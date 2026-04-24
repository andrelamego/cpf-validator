package io.github.andrelamego.brValidator.config;

import io.github.andrelamego.brValidator.service.CnpjValidationService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CnpjValidatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CnpjValidationService cnpjValidationService() {
        return new CnpjValidationService();
    }
}
