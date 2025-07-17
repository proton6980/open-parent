package com.open.starter.json.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.open.starter.json.handler.LongSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * jackson自动化配置
 *
 * @author godLian
 */
@Slf4j
@Configuration
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // 全局配置序列化返回 JSON 处理
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(Long.class, LongSerializer.INSTANCE);
            javaTimeModule.addSerializer(Long.TYPE, LongSerializer.INSTANCE);
            javaTimeModule.addSerializer(BigInteger.class, LongSerializer.INSTANCE);
            javaTimeModule.addSerializer(BigDecimal.class, LongSerializer.instance);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
//            javaTimeModule.addDeserializer(Date.class, new CustomDateDeserializer());
            builder.modules(javaTimeModule);
            builder.timeZone(TimeZone.getDefault());
            log.info("初始化 jackson 配置");
        };
    }
}
