package com.open.extend.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    private Integer maxRetryCount;

    /**
     * 密码锁定时间（默认10分钟）
     */
    private Integer lockTime;

}
