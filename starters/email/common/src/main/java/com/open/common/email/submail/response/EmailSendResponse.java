package com.open.common.email.submail.response;

import lombok.Data;

/**
 * 邮件发送结果
 *
 * @author open
 */
@Data
public class EmailSendResponse {

    private String status;

    private String to;

    private String send_id;
}
