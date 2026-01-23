package com.open.starter.swarmbots.telegram;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.open.common.core.utils.OkHttpUtil;
import com.open.starter.swarmbots.BotClient;
import com.open.starter.swarmbots.telegram.properties.TelegramBotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * telegram bot 客户端
 *
 * @author open
 */
@Slf4j
@RequiredArgsConstructor
public class TelegramBotClient implements BotClient {

    private final TelegramBotProperties properties;

    @Override
    public void sendMessage(String message) {
        log.info("send message: {}", message);
        if (StrUtil.isBlank(message) || !properties.getEnabled()) {
            return;
        }
        if (StrUtil.isBlank(properties.getDomain()) || StrUtil.isBlank(properties.getToken())
                || Objects.isNull(properties.getBotId())) {
            throw new RuntimeException("please configure telegram bot setting");
        }

        JSONObject json = new JSONObject()
                .set("chat_id", properties.getBotId())
                .set("text", message);

        String result = OkHttpUtil.post(StrUtil.format("{}/bot{}/sendMessage", properties.getDomain(),
                properties.getToken(), properties.getBotId()), json.toStringPretty());
        log.info("send message result: {}", result);
    }
}
