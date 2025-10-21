package com.open.starter.http.config;

import com.open.commons.ssl.CompositeX509TrustManager;
import com.open.starter.http.properties.OkHttpProperties;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * okhttp自动配置
 *
 * @author open
 */
@ConditionalOnMissingBean(OkHttpClient.class)
@EnableConfigurationProperties(OkHttpProperties.class)
@Configuration
@ConditionalOnProperty(prefix = "open.okhttp", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OkHttpAutoConfiguration {

    @Bean
    public OkHttpClient okHttpClient(OkHttpProperties properties) throws Exception {
        // 加载自定义证书
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getClass().getResourceAsStream("/certs/custom-cert.crt");
        Certificate customCa = cf.generateCertificate(caInput);
        caInput.close();

        // 创建包含自定义证书的 KeyStore
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("custom-ca", customCa);

        // 创建 TrustManagerFactory 来信任自定义证书
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // 获取系统默认的 TrustManager
        TrustManagerFactory systemTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        systemTmf.init((KeyStore) null);

        // 合并 TrustManager
        List<X509TrustManager> trustManagers = new ArrayList<>();

        // 添加自定义证书信任管理器
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                trustManagers.add((X509TrustManager) tm);
            }
        }

        // 添加系统默认信任管理器
        for (TrustManager tm : systemTmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                trustManagers.add((X509TrustManager) tm);
            }
        }

        CompositeX509TrustManager trustManager = new CompositeX509TrustManager(trustManagers);
        // 信任任何连接
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .retryOnConnectionFailure(properties.getRetry())
                .connectionPool(properties.getPool())
                .connectTimeout(properties.getConnectTimeout())
                .readTimeout(properties.getReadTimeout())
                .writeTimeout(properties.getWriteTimeout())
                .build();
    }
}
