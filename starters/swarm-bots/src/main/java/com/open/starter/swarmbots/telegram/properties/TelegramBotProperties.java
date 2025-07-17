package com.open.starter.swarmbots.telegram.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * telegram bot 配置
 *
 * @author godLian
 */
@Data
@ConfigurationProperties(prefix = "open.telegram.bot")
public class TelegramBotProperties {
    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 域名
     */
    private String domain;

    /**
     * token
     */
    private String token;

    /**
     * 机器人id
     */
    private Long botId;
}
