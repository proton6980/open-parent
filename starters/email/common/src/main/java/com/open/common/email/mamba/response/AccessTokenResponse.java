package com.open.common.email.mamba.response;

import lombok.Data;

/**
 * 获取访问令牌响应
 *
 * @author open
 */
@Data
public class AccessTokenResponse {
    /**
     * token
     */
    private String token;
    /**
     * 过期时长，单位：秒
     */
    private Long expire;
}
