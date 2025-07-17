package com.open.starter.web.config;

import com.open.starter.web.filter.CryptoFilter;
import com.open.starter.web.properties.ApiDecryptProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;

/**
 * api 解密自动配置
 *
 * @author wdhcr
 */
@Configuration
@EnableConfigurationProperties(ApiDecryptProperties.class)
@ConditionalOnProperty(value = "open.api-decrypt.enabled", havingValue = "true")
public class OpenApiDecryptAutoConfiguration {

    @Bean
    public FilterRegistrationBean<CryptoFilter> cryptoFilterRegistration(ApiDecryptProperties properties) {
        FilterRegistrationBean<CryptoFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new CryptoFilter(properties));
        registration.addUrlPatterns("/*");
        registration.setName("cryptoFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }
}

