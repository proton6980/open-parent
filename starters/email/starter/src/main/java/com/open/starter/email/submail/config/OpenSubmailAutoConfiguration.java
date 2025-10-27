package com.open.starter.email.submail.config;

import com.open.starter.email.submail.properties.SubmailProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * submail自动化配置
 *
 * @author open
 */
@Configuration
@EnableConfigurationProperties(value = SubmailProperties.class)
public class OpenSubmailAutoConfiguration {

//    @Bean
//    @ConditionalOnProperty(prefix = "open.email", value = "submail.enabled", havingValue = "true")
//    public SubmailEMailUtil submailEMailUtil(SubmailProperties submailProperties) {
//        return new SubmailEMailUtil(submailProperties.getAppid(), submailProperties.getAppKey());
//    }
}
