package com.open.commons.exception;

/**
 * 业务异常
 *
 * @author open
 */
public class BusinessException extends OpenException {
    private static final long serialVersionUID = 1L;

    public BusinessException(Integer code, String key, Object... args) {
        super(null, code, key, args);
    }

    public BusinessException(String key, Object... args) {
        super(null, null, key, args);
    }
}
