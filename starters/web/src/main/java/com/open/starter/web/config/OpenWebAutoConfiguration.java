package com.open.starter.web.config;

import com.open.starter.web.handler.OpenWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * open web 自动化配置
 *
 * @author godLian
 */
@Configuration
public class OpenWebAutoConfiguration {

    /**
     * 全局异常处理
     */
    @Bean
    public OpenWebExceptionHandler globalExceptionHandler() {
        return new OpenWebExceptionHandler();
    }
}
