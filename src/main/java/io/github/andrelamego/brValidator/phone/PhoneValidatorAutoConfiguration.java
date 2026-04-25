package io.github.andrelamego.brValidator.phone;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class PhoneValidatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PhoneValidationService phoneValidationService() {
        return new PhoneValidationService();
    }
}
