package com.open.starter.mybatisplus.handler;

import com.open.commons.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * mybatis-plus全局异常处理
 *
 * @author open
 */
@Slf4j
@RestControllerAdvice
public class OpenMybatisPlusExceptionHandler {

    /**
     * 违反唯一性约定
     *
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<Void> handleSqlException(SQLIntegrityConstraintViolationException e) {
        log.error("违反唯一性约定:", e);
        return R.fail(500, "Violation of the Uniqueness Convention");
    }
}
