package com.open.commons.exception;

import cn.hutool.core.util.StrUtil;
import com.open.commons.exception.enums.ExceptionModule;
import lombok.Getter;

/**
 * open异常
 *
 * @author open
 */
@Getter
public class OpenException extends BaseException {
    private static final long serialVersionUID = 1L;

    public OpenException(ExceptionModule module, int code, String message) {
        super(module.name(), code, message);
    }

    public OpenException(String module, int code, String message, Object... args) {
        super(module, code, StrUtil.format(message, args));
    }
}
