//package com.open.starter.email.submail;
//
//import cn.hutool.core.lang.Dict;
//import cn.hutool.core.util.StrUtil;
//import com.open.commons.utils.JacksonUtils;
//import com.open.commons.utils.OkHttpUtil;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Submail邮件发送工具类
// * 基于Submail的mail/send API实现
// * <br>
// * <a href="https://en.mysubmail.com/documents/4MfRT2">接口文档</a>
// *
// * @author open
// */
//@Slf4j
//public class SubmailEMailUtil {
//    /**
//     * Submail API配置
//     */
//    private String apiUrl;
//    /**
//     * 应用ID
//     */
//    private String appId;
//    /**
//     * 应用密钥
//     */
//    private String appKey;
//    /**
//     * 其他配置
//     */
//    private String signType = "normal";
//    private boolean asynchronous = false;
//
//    public SubmailEMailUtil(String appId, String appKey) {
//        this(appId, appKey, "https://api-v4.mysubmail.com/mail/send.json");
//    }
//
//    public SubmailEMailUtil(String appId, String appKey, String apiUrl) {
//        this.appId = appId;
//        this.appKey = appKey;
//        this.apiUrl = apiUrl;
//    }
//
//    public SubmailEMailUtil(String appId, String appKey, String apiUrl, String signType, boolean asynchronous) {
//        this.appId = appId;
//        this.appKey = appKey;
//        this.apiUrl = apiUrl;
//        this.signType = signType;
//        this.asynchronous = asynchronous;
//    }
//
//    // 保留空构造函数以保持向后兼容性
//    public SubmailEMailUtil() {
//    }
//
//    /**
//     * 发送简单文本邮件
//     *
//     * @param to      收件人邮箱，多个用逗号分隔
//     * @param from    发件人邮箱
//     * @param subject 邮件主题
//     * @param text    邮件内容
//     * @return API发送是否成功
//     */
//    public boolean sendTextMail(String to, String from, String subject, String text) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("to", to);
//        params.put("from", from);
//        params.put("subject", subject);
//        params.put("text", text);
//        return sendMail(params);
//    }
//
//    /**
//     * 发送HTML邮件
//     *
//     * @param to      收件人邮箱，多个用逗号分隔
//     * @param from    发件人邮箱
//     * @param subject 邮件主题
//     * @param html    HTML邮件内容
//     * @return API发送是否成功
//     */
//    public boolean sendHtmlMail(String to, String from, String subject, String html) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("to", to);
//        params.put("from", from);
//        params.put("subject", subject);
//        params.put("html", html);
//        return sendMail(params);
//    }
//
//    /**
//     * 发送带附件的邮件
//     *
//     * @param to          收件人邮箱，多个用逗号分隔
//     * @param from        发件人邮箱
//     * @param subject     邮件主题
//     * @param text        邮件内容
//     * @param attachments 附件文件数组
//     * @return API发送是否成功
//     */
//    public boolean sendMailWithAttachments(String to, String from, String subject, String text, File[] attachments) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("to", to);
//        params.put("from", from);
//        params.put("subject", subject);
//        params.put("text", text);
//        return sendMail(params, attachments);
//    }
//
//    /**
//     * 发送带变量的邮件
//     *
//     * @param to      收件人邮箱，多个用逗号分隔
//     * @param from    发件人邮箱
//     * @param subject 邮件主题
//     * @param text    邮件内容
//     * @param vars    变量映射
//     * @return API发送是否成功
//     */
//    public boolean sendMailWithVars(String to, String from, String subject, String text, Map<String, Object> vars) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("to", to);
//        params.put("from", from);
//        params.put("subject", subject);
//        params.put("text", text);
//        params.put("vars", JacksonUtils.toJsonString(vars));
//        return sendMail(params);
//    }
//
//    /**
//     * 发送基础邮件
//     *
//     * @param params 邮件参数
//     * @return API发送是否成功
//     */
//    public boolean sendMail(Map<String, Object> params) {
//        return sendMail(params, null);
//    }
//
//    /**
//     * 发送邮件核心方法
//     *
//     * @param params      邮件参数
//     * @param attachments 附件文件数组
//     * @return API发送是否成功
//     */
//    public boolean sendMail(Map<String, Object> params, File[] attachments) {
//        try {
//            // 添加基础参数
//            params.put("appid", this.appId);
//            params.put("signature", this.appKey);
//
//            // 添加签名类型（如果未指定）
//            if (!params.containsKey("sign_type")) {
//                params.put("sign_type", this.signType);
//            }
//
//            // 添加异步发送选项（如果未指定）
//            if (!params.containsKey("asynchronous")) {
//                params.put("asynchronous", this.asynchronous ? "true" : "false");
//            }
//
//            // 构建请求体
//            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM);
//
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                if (entry.getValue() != null) {
//                    bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue().toString());
//                }
//            }
//
//            // 添加附件
//            if (attachments != null) {
//                for (File attachment : attachments) {
//                    if (attachment != null && attachment.exists()) {
//                        bodyBuilder.addFormDataPart("attachments", attachment.getName(),
//                                RequestBody.create(attachment, MediaType.parse("application/octet-stream")));
//                    }
//                }
//            }
//
//            // 发送请求
//            String result = OkHttpUtil.post(this.apiUrl, bodyBuilder.build());
//            Dict dict = JacksonUtils.parseMap(result);
//            String status = dict.get("status", "error");
//            if (StrUtil.equals("error", status)) {
//                throw new RuntimeException("邮件发送失败【" + dict.get("code") + "】: " + dict.get("msg"));
//            }
//            return true;
//        } catch (Exception e) {
//            log.error("邮件发送异常", e);
//            return false;
//        }
//    }
//}