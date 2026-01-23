package com.open.common.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * open封装异常
 *
 * @author open
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OpenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String defaultMessage;

    public OpenException(String module, Integer code, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public OpenException(Integer code, String defaultMessage) {
        this(null, code, defaultMessage);
    }


    public OpenException(String defaultMessage) {
        this(null, null, defaultMessage);
    }
}
