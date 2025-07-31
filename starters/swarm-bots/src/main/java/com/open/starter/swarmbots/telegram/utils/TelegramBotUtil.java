package com.open.starter.swarmbots.telegram.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.open.starter.swarmbots.telegram.TelegramBotClient;

/**
 * telegram bot工具类
 *
 * @author open
 */
public final class TelegramBotUtil {
    private final static TelegramBotClient CLIENT = SpringUtil.getBean(TelegramBotClient.class);

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public static void sendMessage(String message) {
        CLIENT.sendMessage(message);
    }

}
