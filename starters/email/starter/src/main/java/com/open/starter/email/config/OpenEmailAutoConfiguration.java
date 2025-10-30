package com.open.starter.email.config;

import com.open.common.email.mamba.MambaEmailClient;
import com.open.common.email.submail.SubmailEmailClient;
import com.open.starter.cache.utils.RedisUtils;
import com.open.starter.email.expand.MambaCacheEmailClient;
import com.open.starter.email.properties.MambaEmailClientProperties;
import com.open.starter.email.properties.SubmailEmailClientProperties;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * email客户端自动化配置
 *
 * @author open
 */
@Configuration
@EnableConfigurationProperties({
        SubmailEmailClientProperties.class,
        MambaEmailClientProperties.class
})
public class OpenEmailAutoConfiguration {

    @Primary
    @Bean
    @ConditionalOnClass(RedisUtils.class)
    @ConditionalOnProperty(prefix = "open.email", value = "mamba.enabled", havingValue = "true")
    public MambaEmailClient mambaCacheEmailClient(MambaEmailClientProperties properties, OkHttpClient okHttpClient) {
        MambaCacheEmailClient client = new MambaCacheEmailClient(properties.getApiKey(), properties.getPrivateKey());
        client.setOkHttpClient(okHttpClient);
        client.setDomain(properties.getDomain());
        return client;
    }

    @Bean
    @ConditionalOnMissingBean(MambaEmailClient.class)
    @ConditionalOnProperty(prefix = "open.email", value = "mamba.enabled", havingValue = "true")
    public MambaEmailClient mambaEmailClient(MambaEmailClientProperties properties, OkHttpClient okHttpClient) {
        MambaEmailClient client = new MambaEmailClient(properties.getApiKey(), properties.getPrivateKey());
        client.setOkHttpClient(okHttpClient);
        client.setDomain(properties.getDomain());
        return client;
    }

    @Bean
    @ConditionalOnMissingBean(SubmailEmailClient.class)
    @ConditionalOnProperty(prefix = "open.email", value = "submail.enabled", havingValue = "true")
    public SubmailEmailClient submailEmailClient(SubmailEmailClientProperties properties, OkHttpClient okHttpClient) {
        SubmailEmailClient client = new SubmailEmailClient(properties.getAppid(), properties.getSignature(), properties.getFrom());
        client.setOkHttpClient(okHttpClient);
        client.setDomain(properties.getDomain());
        return client;
    }
}
