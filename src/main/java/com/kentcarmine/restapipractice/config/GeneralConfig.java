package com.kentcarmine.restapipractice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class GeneralConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

//    @Autowired
//    @Bean
//    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
//        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//        bean.setValidationMessageSource(messageSource);
//        return bean;
//    }
}
