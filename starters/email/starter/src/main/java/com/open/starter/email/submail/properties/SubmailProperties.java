package com.open.starter.email.submail.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * submail 邮件发送配置属性
 *
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.mail.submail")
public class SubmailProperties {
    /**
     * 启用|禁用
     */
    private boolean enabled;
    /**
     * 应用appid
     */
    private String appid;
    /**
     * 应用app-key
     */
    private String appKey;
}
