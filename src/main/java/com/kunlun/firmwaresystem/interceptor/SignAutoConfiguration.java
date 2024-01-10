package com.kunlun.firmwaresystem.interceptor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SignAutoConfiguration {

    @Bean
    public SignStreamFilter initSignFilter() {
        return new SignStreamFilter();
    }

    @Bean
    public FilterRegistrationBean webAuthFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(initSignFilter());
        registration.setName("signFilter");
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }
}

