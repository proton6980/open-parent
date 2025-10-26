package com.open.starter.http.config;

import com.open.common.http.ssl.CompositeX509TrustManager;
import com.open.common.http.ssl.DefaultX509TrustManager;
import com.open.starter.http.properties.OkHttpProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * okhttp自动配置
 *
 * @author open
 */
@Slf4j
@EnableConfigurationProperties(OkHttpProperties.class)
@Configuration
@ConditionalOnMissingBean(OkHttpClient.class)
@ConditionalOnProperty(prefix = "open.okhttp", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OkHttpAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "open.okhttp", name = "ssl-pattern", havingValue = "CUSTOM")
    public OkHttpClient customOkHttpClient(OkHttpProperties properties) throws Exception {
        // 合并 TrustManager
        List<X509TrustManager> trustManagers = new ArrayList<>();

        List<String> certs = properties.getCerts();
        if (!CollectionUtils.isEmpty(certs)) {
            // 加载自定义证书
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            List<Certificate> certificates = certs.stream()
                    .filter(StringUtils::hasText)
                    .map(cert -> {
                        InputStream inputStream = null;
                        try {
                            if (cert.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
                                File file = new File(cert.substring(ResourceUtils.FILE_URL_PREFIX.length()));
                                if (file.exists()) {
                                    inputStream = Files.newInputStream(file.toPath());
                                }
                            } else {
                                inputStream = getClass().getResourceAsStream(cert.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length()));
                            }
                            if (Objects.nonNull(inputStream)) {
                                return cf.generateCertificate(inputStream);
                            }
                        } catch (IOException e) {
                            log.warn("无法加载自定义证书 -> " + cert, e);
                        } catch (CertificateException e) {
                            log.warn("无法生成自定义证书 -> " + cert, e);
                        } finally {
                            try {
                                if (Objects.nonNull(inputStream)) {
                                    inputStream.close();
                                }
                            } catch (IOException e) {
                                log.warn("无法关闭自定义证书输入流 -> " + cert, e);
                            }
                        }
                        return null;
                    }).collect(Collectors.toList());
            // 创建包含自定义证书的 KeyStore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            // 加载所有证书到 keyStore 中
            for (int i = 0; i < certificates.size(); i++) {
                keyStore.setCertificateEntry("custom-ca-" + i, certificates.get(i));
            }

            // 创建 TrustManagerFactory 来信任自定义证书
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            // 添加自定义证书信任管理器
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    trustManagers.add((X509TrustManager) tm);
                }
            }
        }

        // 获取系统默认的 TrustManager
        TrustManagerFactory systemTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        systemTmf.init((KeyStore) null);
        // 添加系统默认信任管理器
        for (TrustManager tm : systemTmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                trustManagers.add((X509TrustManager) tm);
            }
        }

        CompositeX509TrustManager trustManager = new CompositeX509TrustManager(trustManagers);
        // 创建支持自定义证书和系统默认证书的SSL上下文
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

    @Bean
    @ConditionalOnProperty(prefix = "open.okhttp", name = "ssl-pattern", havingValue = "TRUST_ALL", matchIfMissing = true)
    public OkHttpClient defaultOkHttpClient(OkHttpProperties properties) throws Exception {
        X509TrustManager x509TrustManager = new DefaultX509TrustManager();
        // 初始化SSL的上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());

        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                // 永远返回true，对所有的host都信任
                .hostnameVerifier((s, sslSession) -> true)
                .retryOnConnectionFailure(properties.getRetry())
                .connectionPool(properties.getPool())
                .connectTimeout(properties.getConnectTimeout())
                .readTimeout(properties.getReadTimeout())
                .writeTimeout(properties.getWriteTimeout())
                .build();
    }
}
