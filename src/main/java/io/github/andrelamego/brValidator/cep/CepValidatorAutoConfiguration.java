package io.github.andrelamego.brValidator.cep;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CepValidatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CepValidationService cepValidationService() {
        return new CepValidationService();
    }
}
