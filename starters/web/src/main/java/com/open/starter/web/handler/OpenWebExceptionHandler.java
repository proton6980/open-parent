package com.open.starter.web.handler;

import com.open.commons.exception.OpenException;
import com.open.commons.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author godLian
 */
@Slf4j
@RestControllerAdvice
public class OpenWebExceptionHandler {

    /**
     * open自定义运行时异常
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(OpenException.class)
    public R<Void> handleSqlException(OpenException e) {
        log.error("{}模块异常:", e.getModule(), e);
        return R.fail(e.getCode(), e.getMessage());
    }
}
