package com.open.starter.http.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * 自动化装配 rest
 *
 * @author open
 */
@Configuration
@AutoConfigureAfter(OkHttpAutoConfiguration.class)
@ConditionalOnMissingBean(RestTemplate.class)
@ConditionalOnClass(RestTemplate.class)
public class RestAutoConfiguration {

    @Bean
    @ConditionalOnBean(OkHttpClient.class)
    public RestTemplate restTemplate(OkHttpClient okHttpClient) {
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return new RestTemplate(factory);
    }
}
