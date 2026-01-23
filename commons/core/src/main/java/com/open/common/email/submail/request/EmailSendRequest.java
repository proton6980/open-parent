package com.open.common.email.submail.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 邮件发送请求
 *
 * @author open
 */
@Data
@Builder
public class EmailSendRequest {
    /**
     * 密钥
     */
    @NotBlank
    private String appid;
    /**
     * 签名
     */
    @NotBlank
    private String signature;
    /**
     * 发件人
     */
    @Email
    private String to;
    /**
     * 收件人
     */
    @Email
    private String from;
    /**
     * 标题
     */
    @NotBlank
    private String subject;
    /**
     * 内容
     */
    @NotBlank
    private String html;
}
