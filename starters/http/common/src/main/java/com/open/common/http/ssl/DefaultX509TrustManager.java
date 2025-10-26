package com.open.common.http.ssl;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * 默认所有链路信任
 *
 * @author open
 */
public class DefaultX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
