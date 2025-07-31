package com.open.starter.sse.config;

import com.open.starter.sse.controller.SseController;
import com.open.starter.sse.core.SseEmitterManager;
import com.open.starter.sse.listener.SseTopicListener;
import com.open.starter.sse.properties.SseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SSE 自动装配
 *
 * @author open
 */
@Configuration
@ConditionalOnProperty(value = "sse.enabled", havingValue = "true")
@EnableConfigurationProperties(SseProperties.class)
public class OpenSseAutoConfiguration {

    @Bean
    public SseEmitterManager sseEmitterManager() {
        return new SseEmitterManager();
    }

    @Bean
    public SseTopicListener sseTopicListener() {
        return new SseTopicListener();
    }

    @Bean
    public SseController sseController(SseEmitterManager sseEmitterManager) {
        return new SseController(sseEmitterManager);
    }

}

