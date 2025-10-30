package com.open.starter.email.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mamba 邮件发送配置属性
 *
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.mail.mamba")
public class MambaEmailClientProperties {
    /**
     * 启用|禁用
     */
    private boolean enabled;
    /**
     * 域名
     */
    private String domain;
    /**
     * 密钥
     */
    private String apiKey;
    /**
     * 私钥
     */
    private String privateKey;
}
