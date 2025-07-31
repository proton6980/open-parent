package com.open.commons.utils;

import cn.hutool.extra.spring.SpringUtil;
import okhttp3.*;

import java.io.IOException;

/**
 * okhttp工具类
 *
 * @author open
 */
public final class OkHttpUtil {
    private static final OkHttpClient CLIENT = SpringUtil.getBean(OkHttpClient.class);


    /**
     * post请求
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return 请求结果
     */
    public static String post(String url, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
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
                return response.body().string();
            } else {
                int code = response.code();
                throw new RuntimeException("request failure，code：" + code + ", msg:" + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
