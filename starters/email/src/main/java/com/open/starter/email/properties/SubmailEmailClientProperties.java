package com.open.starter.email.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * submail 邮件发送配置属性
 *
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.mail.submail")
public class SubmailEmailClientProperties {
    /**
     * 启用|禁用
     */
    private boolean enabled;
    /**
     * 域名
     */
    private String domain;
    /**
     * 应用appid
     */
    private String appid;
    /**
     * 应用签名
     */
    private String signature;
    /**
     * 发件人邮箱地址
     */
    private String from;
}
