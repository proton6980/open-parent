package com.open.common.email.mamba.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 邮件OTP发送请求
 *
 * @author open
 */
@Data
@Builder
public class EmailOtpRequest {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 内容
     */
    private Content content;
    /**
     * 收件人集合
     */
    private List<Recipient> recipients;
    /**
     * 发件人
     */
    private From from;

    /**
     * 发件人
     */
    @Builder
    public static class From {
        /**
         * 邮箱
         */
        private String email;
        /**
         * 姓名
         */
        private String name;
    }

    /**
     * 收件人
     */
    @Builder
    public static class Recipient {
        /**
         * 地址
         */
        private Address address;
        /**
         * 元数据
         * <p>对应html的数据填充</p>
         */
        private Map<String, String> metadata;
    }

    /**
     * 地址
     */
    @Builder
    public static class Address {
        /**
         * 邮箱
         */
        private String email;
        /**
         * 姓名
         */
        private String name;
    }

    /**
     * 邮件内容
     */
    @Builder
    public static class Content {
        /**
         * 主题
         */
        private String subject;
        /**
         * html内容
         */
        private String html;
    }
}
