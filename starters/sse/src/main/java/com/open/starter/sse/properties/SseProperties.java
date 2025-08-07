package com.open.starter.sse.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SSE 配置项
 *
 * @author open
 */
@Data
@ConfigurationProperties("open.sse")
public class SseProperties {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 路径
     */
    private String path;
}

