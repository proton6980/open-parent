package com.open.starter.swarmbots;

/**
 * 机器人客户端
 *
 * @author open
 */
public interface BotClient {

    /**
     * 发送消息
     *
     * @param message 内容
     */
    void sendMessage(String message);
}
