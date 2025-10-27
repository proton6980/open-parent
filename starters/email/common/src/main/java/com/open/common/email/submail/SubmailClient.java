package com.open.common.email.submail;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.open.common.email.submail.request.EmailSendRequest;
import com.open.common.email.submail.response.EmailSendResponse;
import com.open.common.http.client.DefaultOkHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

/**
 * submail客户端
 *
 * @author open
 */
@Slf4j
@RequiredArgsConstructor
public class SubmailClient {
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
        try (Response response = getOkHttpClient().newCall(new Request.Builder()
                .url("https://api.mysubmail.com/mail/send.json")
                .post(RequestBody.create(JSONUtil.toJsonStr(request), MediaType.parse("application/json; charset=utf-8")))
                .build()).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                JSONObject body = JSONUtil.parseObj(response.body().string());
                if ("success".equals(body.getStr("status"))) {
                    EmailSendResponse res = JSONUtil.parseArray(body).get(0, EmailSendResponse.class);
                    return "success".equals(res.getStatus());
                }
                log.warn("获取访问令牌失败，{}", body);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public OkHttpClient getOkHttpClient() {
        return Objects.nonNull(okHttpClient) ? okHttpClient : DefaultOkHttpClient.getInstance();
    }
}
