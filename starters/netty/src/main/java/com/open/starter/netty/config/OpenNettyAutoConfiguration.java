package com.open.starter.netty.config;

import com.open.starter.netty.properties.NettyTcpProperties;
import com.open.starter.netty.properties.NettyWebSocketProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * netty自动配置
 *
 * @author open
 */
@Configuration
@EnableConfigurationProperties({
        NettyWebSocketProperties.class,
        NettyTcpProperties.class
})
@ComponentScan(value = "com.open.starter.netty")
public class OpenNettyAutoConfiguration {
}
