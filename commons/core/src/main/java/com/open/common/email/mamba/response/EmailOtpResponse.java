package com.open.common.email.mamba.response;

import lombok.Data;

/**
 * 邮箱OTP发送响应
 *
 * @author open
 */
@Data
public class EmailOtpResponse {
    /**
     * 邮件发送失败数量
     */
    private int failedCount;
    /**
     * 邮件发送成功数量
     */
    private int successCount;
}
