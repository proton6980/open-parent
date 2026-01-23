package com.open.common.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.common.core.utils.I18nUtils;
import com.open.common.core.utils.JacksonUtils;
import com.open.common.core.utils.OkHttpUtil;
import com.open.common.core.utils.translate.ITranslateClient;
import com.open.common.spring.utils.SpringUtils;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * 工具类自动配置
 * <p>
 * 这个配置类的作用是在 Spring 容器初始化完成后，
 * 将容器中管理的 ObjectMapper Bean 注入到 JacksonUtils 中。
 */
@Configuration
@RequiredArgsConstructor
public class OpenUtilAutoConfiguration {
    private final ObjectMapper objectMapper;

    // 使用 @PostConstruct 注解，在 Spring 完成 Bean 的初始化之后执行
    @PostConstruct
    public void init() {
        // 调用 JacksonUtils 的静态 init 方法，完成注入
        JacksonUtils.init(objectMapper);

        if (SpringUtils.containsBean(OkHttpClient.class)) {
            // 调用 OkHttpUtil 的静态 init 方法，完成注入
            OkHttpUtil.init(SpringUtils.getBean(OkHttpClient.class));
        }

        // 调用 ITranslateClient 的静态 init 方法，完成注入
        I18nUtils.init(SpringUtils.getBean(ITranslateClient.class));
    }

    @Bean
    @ConditionalOnMissingBean
    public ITranslateClient translateClient(MessageSource messageSource) {
        return new ITranslateClient() {
            @Override
            public String translate(Locale target, String text) {
                return messageSource.getMessage(text, new Object[0], target);
            }

            @Override
            public String translate(Locale target, String text, Object... args) {
                return messageSource.getMessage(text, args, target);
            }
        };
    }
}