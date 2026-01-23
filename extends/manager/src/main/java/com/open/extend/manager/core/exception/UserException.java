package com.open.extend.manager.core.exception;

import com.open.common.spring.exception.OpenBusinessException;

/**
 * 用户异常
 *
 * @author open
 */
public class UserException extends OpenBusinessException {

    public UserException(String message, Object... args) {
        this(null, message, args);
    }

    public UserException(Integer code, String message, Object... args) {
        super("extend manager user", code, message, args);
    }
}
