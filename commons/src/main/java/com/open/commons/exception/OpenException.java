package com.open.commons.exception;

import cn.hutool.core.util.StrUtil;
import com.open.commons.exception.enums.ExceptionModule;
import lombok.Getter;

/**
 * open异常
 *
 * @author godLian
 */
@Getter
public class OpenException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 模块
     */
    private final String module;
    /**
     * 错误码
     */
    private final int code;

    public OpenException(ExceptionModule module, int code, String message) {
        this(module.name(), code, message);
    }

    public OpenException(String module, int code, String message) {
        super(message);
        this.module = module;
        this.code = code;
    }

    public OpenException(String module, int code, String message, Object... args) {
        super(StrUtil.format(message, args));
        this.module = module;
        this.code = code;
    }
}
