package com.open.starter.http.properties;

import com.open.common.core.http.SSLPattern;
import lombok.Data;
import okhttp3.ConnectionPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * okhttp 配置属性
 *
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.okhttp")
public class OkHttpProperties {
    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * ssl 模式
     */
    private SSLPattern sslPattern = SSLPattern.TRUST_ALL;

    /**
     * 证书路径
     * <p>默认在 classpath:**.pem 或 classpath:**.crt 或 classpath:**.cer</p>
     * <p>外部文件 file:**.pem 或 file:**.crt 或 file:**.cer</p>
     */
    private List<String> certs;

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
