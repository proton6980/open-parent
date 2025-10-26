package com.open.common.email.mamba;

import com.open.common.email.mamba.request.AccessTokenRequest;
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
@Getter
@AllArgsConstructor
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
    private OkHttpClient okHttpClient;

    public MambaEmailClient(String apiKey, String privateKey) {
        this.apiKey = apiKey;
        this.privateKey = privateKey;
    }

    public String authenticate(Duration expire) {
        AccessTokenRequest request = AccessTokenRequest.builder()
                .apiKey(getApiKey())
                .privateKey(getPrivateKey())
                .expire(expire)
                .build();
        try (Response response = getOkHttpClient().newCall(new Request.Builder()
                .url("https://send.mambasms.com/open/api/v1/access-token")
                .post(RequestBody.create(request.toJson(), MediaType.parse("application/json; charset=utf-8")))
                .build()).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                String json = response.body().string();
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public OkHttpClient getOkHttpClient() {
        return Objects.isNull(this.okHttpClient) ? DefaultOkHttpClient.getInstance() : this.okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
