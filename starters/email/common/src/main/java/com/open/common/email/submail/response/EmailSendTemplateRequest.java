package com.open.common.email.submail.response;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EmailSendTemplateRequest {
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
     * 收件人
     */
    @Email
    private String to;
    /**
     * 模板ID
     */
    @NotBlank
    private String project;
    /**
     * 动态参数
     */
    private String vars;
}
