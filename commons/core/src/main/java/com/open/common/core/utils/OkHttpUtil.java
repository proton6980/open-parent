package com.open.common.core.utils;

import com.open.common.core.http.client.DefaultOkHttpClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

/**
 * okhttp工具类
 *
 * @author open
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OkHttpUtil {

    // 静态持有一个默认的 OkHttpClient 实例。
    private static OkHttpClient CLIENT = DefaultOkHttpClient.getInstance();

    /**
     *
     * 初始化方法，用于从外部(如Spring容器)注入一个 OkHttpClient 实例。
     *
     * @param springOkHttpClient 外部配置的 OkHttpClient
     */
    public static void init(OkHttpClient springOkHttpClient) {
        if (springOkHttpClient != null) {
            CLIENT = springOkHttpClient;
        }
    }

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
