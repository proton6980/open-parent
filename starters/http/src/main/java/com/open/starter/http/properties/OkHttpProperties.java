package com.open.starter.http.properties;

import lombok.Data;
import okhttp3.ConnectionPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * okhttp 配置属性
 *
 * @author godLian
 */
@Data
@ConfigurationProperties(prefix = "open.okhttp")
public class OkHttpProperties {
    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否重试
     */
    private Boolean retry = true;

    /**
     * 连接超时
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * 读取超时
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration readTimeout = Duration.ofSeconds(10);

    /**
     * 写超时
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration writeTimeout = Duration.ofSeconds(10);

    /**
     * 连接池
     */
    private Pool pool = new Pool();

    @Data
    public static class Pool {
        /**
         * 最大空闲连接数
         */
        private Integer maxIdle = 30;
        /**
         * 空闲连接保持时长
         */
        @DurationUnit(ChronoUnit.MINUTES)
        private Duration keepAlive = Duration.ofMinutes(5);
    }

    public ConnectionPool getPool() {
        return new ConnectionPool(pool.getMaxIdle(), pool.getKeepAlive().getSeconds(), TimeUnit.SECONDS);
    }
}
