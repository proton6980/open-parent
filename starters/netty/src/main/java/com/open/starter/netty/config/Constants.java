package com.open.starter.netty.config;

import io.netty.util.AttributeKey;

/**
 * 常量
 *
 * @author open
 */
public interface Constants {
    /**
     * 匿名websocket连接
     */
    String GLOBAL_WEBSOCKET_CHANNELS = "global:websocket:channels";
    /**
     * 用户websocket连接
     */
    String GLOBAL_WEBSOCKET_CHANNELS_USER = "global:websocket:user";
    /**
     * 用户id属性名
     */
    AttributeKey<Long> USER_ID_KEY = AttributeKey.valueOf("userId");
}
