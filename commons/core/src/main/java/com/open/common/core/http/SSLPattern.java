package com.open.common.core.http;

import lombok.Getter;

/**
 * ssl证书模式
 *
 * @author open
 */
@Getter
public enum SSLPattern {
    /**
     * 自定义
     */
    CUSTOM,
    /**
     * 信任所有
     */
    TRUST_ALL
}
