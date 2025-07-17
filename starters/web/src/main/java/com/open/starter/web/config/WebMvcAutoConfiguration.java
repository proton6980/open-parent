package com.open.starter.web.config;

import com.open.starter.web.interceptor.ResponseWrapperInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web mvc自动化配置
 *
 * @author godLian
 */
@Configuration
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    /**
     * 响应包装拦截器
     */
    @Bean
    public ResponseWrapperInterceptor responseWrapperInterceptor() {
        return new ResponseWrapperInterceptor();
    }
}
