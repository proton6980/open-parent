package com.open.common.http.ssl;

import lombok.RequiredArgsConstructor;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

/**
 * 复合链路信任
 *
 * @author open
 */
@RequiredArgsConstructor
public class CompositeX509TrustManager implements X509TrustManager {

    private final List<X509TrustManager> trustManagers;

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager tm : trustManagers) {
            try {
                tm.checkClientTrusted(chain, authType);
                return;
            } catch (CertificateException ignored) {
            }
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager tm : trustManagers) {
            try {
                tm.checkServerTrusted(chain, authType);
                return;
            } catch (CertificateException ignored) {
            }
        }
        throw new CertificateException("None of the TrustManagers trust this certificate chain");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return trustManagers.stream()
                .flatMap(tm -> Arrays.stream(tm.getAcceptedIssuers()))
                .toArray(X509Certificate[]::new);
    }
}
