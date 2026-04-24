package com.lamego.brValidator.config;

import com.lamego.brValidator.service.CpfValidationService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CpfValidatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CpfValidationService cpfValidationService() {
        return new CpfValidationService();
    }
}