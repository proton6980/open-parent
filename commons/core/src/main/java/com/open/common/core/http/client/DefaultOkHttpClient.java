package com.open.common.core.http.client;

import com.open.common.core.http.ssl.DefaultX509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.ConnectionPool;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * 创建默认 OkHttpClient
 *
 * @author open
 */
public class DefaultOkHttpClient {

    private static final OkHttpClient DEFAULT_CLIENT = createDefaultClient();

    /**
     * 获取默认的 OkHttpClient 实例
     */
    public static OkHttpClient getInstance() {
        return DEFAULT_CLIENT;
    }

    /**
     * 创建默认配置的 OkHttpClient
     */
    private static OkHttpClient createDefaultClient() {
        try {
            X509TrustManager x509TrustManager = new DefaultX509TrustManager();
            // 初始化SSL的上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                    // 永远返回true，对所有的host都信任
                    .hostnameVerifier((s, sslSession) -> true)
                    .retryOnConnectionFailure(true)
                    .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create default OkHttpClient", e);
        }
    }
}

