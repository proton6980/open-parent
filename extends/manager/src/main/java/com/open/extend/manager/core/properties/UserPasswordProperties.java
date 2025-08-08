package com.open.extend.manager.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 用户密码配置
 *
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.user.password")
public class UserPasswordProperties {

    /**
     * 密码最大错误次数
     */
    private Integer maxRetryCount = 3;

    /**
     * 密码锁定时间（默认10分钟）
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration lockTime = Duration.ofMinutes(10);

}
