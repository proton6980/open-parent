package com.open.commons.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 基础异常
 *
 * @author open
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private final String module;

    /**
     * 错误码
     */
    private final Integer code;

    public BaseException(String module, Integer code, String message) {
        super(message);
        this.module = module;
        this.code = code;
    }

    public BaseException(String module, String message) {
        this(module, null, message);
    }

    public BaseException(Integer code, String message) {
        this(null, code, message);
    }

    public BaseException(Integer code) {
        this(null, code, null);
    }

    public BaseException(String message) {
        this(null, null, message);
    }

}
