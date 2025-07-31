package com.open.starter.http.config;

import com.open.starter.http.properties.OkHttpProperties;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * okhttp自动配置
 *
 * @author open
 */
@ConditionalOnMissingBean(OkHttpClient.class)
@EnableConfigurationProperties(OkHttpProperties.class)
@Configuration
@ConditionalOnProperty(prefix = "open.okhttp", name = "enabled", havingValue = "true")
public class OkHttpAutoConfiguration {

    public X509TrustManager x509TrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    };

    @Bean
    public OkHttpClient okHttpClient(OkHttpProperties properties) throws Exception {
        // 信任任何连接
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                .retryOnConnectionFailure(properties.getRetry())
                .connectionPool(properties.getPool())
                .connectTimeout(properties.getConnectTimeout())
                .readTimeout(properties.getReadTimeout())
                .writeTimeout(properties.getWriteTimeout())
                .build();
    }
}
