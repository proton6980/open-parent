package com.open.starter.netty.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * tcp属性
 *
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.netty.tcp")
public class NettyTcpProperties {

    /**
     * 端口
     */
    private Integer port = 8880;

}
