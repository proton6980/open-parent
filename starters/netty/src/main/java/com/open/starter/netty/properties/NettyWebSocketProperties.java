package com.open.starter.netty.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * websocket属性
 *
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.netty.websocket")
public class NettyWebSocketProperties {

    /**
     * 端口
     */
    private Integer port = 8880;

}
