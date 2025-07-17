package com.open.starter.swarmbots.telegram.config;

import com.open.starter.swarmbots.BotClient;
import com.open.starter.swarmbots.telegram.TelegramBotClient;
import com.open.starter.swarmbots.telegram.properties.TelegramBotProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * telegram bot自动化配置
 *
 * @author godLian
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "open.telegram.bot", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotAutoConfiguration {

    @Bean
    public BotClient telegramBotClient(TelegramBotProperties properties) {
        return new TelegramBotClient(properties);
    }

}
