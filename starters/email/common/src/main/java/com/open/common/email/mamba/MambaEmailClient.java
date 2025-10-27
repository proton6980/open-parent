package com.open.common.email.mamba;

import cn.hutool.json.JSONUtil;
import com.open.common.email.mamba.request.AccessTokenRequest;
import com.open.common.email.mamba.request.EmailOtpRequest;
import com.open.common.http.client.DefaultOkHttpClient;
import lombok.*;
import okhttp3.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * 曼巴邮件客户端
 *
 * @author open
 */
@RequiredArgsConstructor
public class MambaEmailClient {
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
        try (Response response = getOkHttpClient().newCall(new Request.Builder()
                .url("https://send.mambasms.com/open/api/v1/access-token")
                .post(RequestBody.create(JSONUtil.toJsonStr(request), MediaType.parse("application/json; charset=utf-8")))
                .build()).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                String json = response.body().string();
                return JSONUtil.parseObj(json).getStr("data");
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
        try (Response response = getOkHttpClient().newCall(new Request.Builder()
                .url("https://send.mambasms.com/api/v1/email/otp")
                .header("X-Mamba-Access-Token", token)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(JSONUtil.toJsonStr(request), MediaType.parse("application/json; charset=utf-8")))
                .build()).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                return 1 == JSONUtil.parseObj(response.body().string()).getInt("code");
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
