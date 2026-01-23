package com.open.common.email.submail;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.open.common.core.utils.OkHttpUtil;
import com.open.common.email.submail.request.EmailSendRequest;
import com.open.common.email.submail.response.EmailSendResponse;
import com.open.common.email.submail.response.EmailSendTemplateRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * submail客户端
 *
 * @author open
 */
@Slf4j
@RequiredArgsConstructor
public class SubmailEmailClient {
    /**
     * 域名
     */
    @Setter
    private String domain = "https://api.mysubmail.com";
    /**
     * 密钥
     */
    private final String appid;
    /**
     * 签名
     */
    private final String signature;
    /**
     * 发件人
     */
    private final String from;

    @Setter
    private OkHttpClient okHttpClient;

    public boolean sendEmail(String subject, String content, String email) {
        EmailSendRequest request = EmailSendRequest.builder()
                .appid(this.appid)
                .signature(this.signature)
                .to(email)
                .from(this.from)
                .subject(subject)
                .html(content)
                .build();
        return this.send("/mail/send", JSONUtil.toJsonStr(request));
    }

    public boolean sendTemplate(String email, String templateId, Map<String, Object> vars) {
        EmailSendTemplateRequest request = EmailSendTemplateRequest.builder()
                .appid(this.appid)
                .signature(this.signature)
                .to(email)
                .project(templateId)
                .vars(JSONUtil.toJsonStr(vars))
                .build();
        return this.send("/mail/xsend", JSONUtil.toJsonStr(request));
    }

    private boolean send(String urlSuffix, String requestBody) {
        log.info("发送邮件请求：{}", requestBody);
        try (Response response = OkHttpUtil.getClient().newCall(new Request.Builder()
                .url(this.domain + urlSuffix)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build()).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                JSONObject body = JSONUtil.parseObj(response.body().string());
                if ("success".equals(body.getStr("status"))) {
                    EmailSendResponse res = JSONUtil.parseArray(body).get(0, EmailSendResponse.class);
                    return "success".equals(res.getStatus());
                }
                log.warn("发送邮件失败，{}", body);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
