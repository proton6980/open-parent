package com.open.starter.social.config;

import com.open.starter.social.properties.SocialProperties;
import com.open.starter.social.utils.AuthRedisStateCache;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Social 配置属性
 *
 * @author godLian
 */
@Configuration
@EnableConfigurationProperties(SocialProperties.class)
public class OpenSocialAutoConfiguration {

    @Bean
    public AuthStateCache authStateCache() {
        return new AuthRedisStateCache();
    }

}
