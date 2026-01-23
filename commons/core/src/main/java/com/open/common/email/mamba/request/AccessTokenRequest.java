package com.open.common.email.mamba.request;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

/**
 * 获取访问令牌请求
 *
 * @author open
 */
@Data
@Builder
public class AccessTokenRequest {
    /**
     * 密钥
     */
    private String apiKey;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 过期时长
     */
    private Duration expire;
}
