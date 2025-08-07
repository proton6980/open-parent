package com.open.extend.gateway.config;

import com.open.extend.gateway.properties.ApiDecryptProperties;
import com.open.extend.gateway.properties.CustomGatewayProperties;
import com.open.extend.gateway.properties.IgnoreWhiteProperties;
import com.open.extend.gateway.handler.SentinelFallbackHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 网关限流配置
 *
 * @author open
 */
@Configuration
@ComponentScan({"com.open.extend.gateway"})
@EnableConfigurationProperties({
        IgnoreWhiteProperties.class,
        CustomGatewayProperties.class,
        ApiDecryptProperties.class
})
public class OpenGatewayAutoConfiguration {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler() {
        return new SentinelFallbackHandler();
    }
}
