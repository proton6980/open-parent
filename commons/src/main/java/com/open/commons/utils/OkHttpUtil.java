package com.open.commons.utils;

import com.open.common.http.client.DefaultOkHttpClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;

/**
 * okhttp工具类
 *
 * @author open
 */
@Slf4j
public final class OkHttpUtil {
    private static final OkHttpClient CLIENT = SpringUtils.containsBean(OkHttpClient.class)
            ? SpringUtils.getBean(OkHttpClient.class) : DefaultOkHttpClient.getInstance();

    /**
     * post请求
     *
     * @param url         请求地址
     * @param requestBody 请求体
     * @return 请求结果
     */
    public static String post(String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }

    /**
     * post请求
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return 请求结果
     */
    public static String post(String url, String json) {
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        return post(url, requestBody);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @return 请求结果
     */
    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return execNewCall(request);
    }

    private static String execNewCall(Request request) {
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    return body.string();
                } else {
                    throw new RuntimeException("response body is null");
                }
            } else {
                int code = response.code();
                throw new RuntimeException("request failure，code：" + code + ", msg:" + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getClient() {
        return CLIENT;
    }
}
