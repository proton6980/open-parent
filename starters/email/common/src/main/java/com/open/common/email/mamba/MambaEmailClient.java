package com.open.common.email.mamba;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.open.common.email.mamba.request.AccessTokenRequest;
import com.open.common.email.mamba.request.EmailOtpRequest;
import com.open.common.email.mamba.response.AccessTokenResponse;
import com.open.common.email.mamba.response.EmailOtpResponse;
import com.open.common.http.client.DefaultOkHttpClient;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * 曼巴邮件客户端
 *
 * @author open
 */
@Slf4j
@RequiredArgsConstructor
public class MambaEmailClient {
    /**
     * 域名
     */
    @Setter
    private String domain = "https://send.mambasms.com";
    /**
     * 密钥
     */
    private final String apiKey;
    /**
     * 私钥
     */
    private final String privateKey;
    /**
     * okhttp客户端
     */
    @Setter
    private OkHttpClient okHttpClient;

    /**
     * 获取访问令牌
     *
     * @param expire 过期时间
     * @return 访问令牌
     */
    public String authenticate(Duration expire) {
        AccessTokenRequest request = AccessTokenRequest.builder()
                .apiKey(this.apiKey)
                .privateKey(this.privateKey)
                .expire(expire)
                .build();
        String requestBody = JSONUtil.toJsonStr(request);
        log.info("邮件发送获取访问令牌请求：{}", requestBody);
        try (Response response = getOkHttpClient().newCall(new Request.Builder()
                .url(this.domain + "/open/api/v1/access-token")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build()).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                JSONObject body = JSONUtil.parseObj(response.body().string());
                if (1 == body.getInt("code")) {
                    return JSONUtil.toBean(body.getJSONObject("data"), AccessTokenResponse.class).getToken();
                }
                log.warn("邮件发送获取访问令牌失败，{}", body);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 发送邮件
     *
     * @param token   令牌
     * @param request 请求
     * @return 是否成功
     */
    public boolean sendEmail(String token, EmailOtpRequest request) {
        String requestBody = JSONUtil.toJsonStr(request);
        log.info("发送邮件请求：{}", requestBody);
        try (Response response = getOkHttpClient().newCall(new Request.Builder()
                .url(this.domain + "/api/v1/email/otp")
                .header("X-Mamba-Access-Token", token)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json; charset=utf-8")))
                .build()).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                JSONObject body = JSONUtil.parseObj(response.body().string());
                if (1 == body.getInt("code")) {
                    return request.getRecipients().size() == JSONUtil.toBean(body.getJSONObject("data"), EmailOtpResponse.class).getSuccessCount();
                }
                log.info("邮件发送失败，" + body.getStr("message"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public OkHttpClient getOkHttpClient() {
        return Objects.isNull(this.okHttpClient) ? DefaultOkHttpClient.getInstance() : this.okHttpClient;
    }
}
