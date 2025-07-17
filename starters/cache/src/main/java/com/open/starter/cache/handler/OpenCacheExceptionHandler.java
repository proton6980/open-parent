package com.open.starter.cache.handler;

import com.baomidou.lock.exception.LockFailureException;
import com.open.commons.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Redis异常处理器
 *
 * @author AprilWind
 */
@Slf4j
@RestControllerAdvice
public class OpenCacheExceptionHandler {

    /**
     * 分布式锁Lock4j异常
     */
    @ExceptionHandler(LockFailureException.class)
    public R<Void> handleLockFailureException(LockFailureException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("获取锁失败了'{}',发生Lock4j异常.", requestURI, e);
        return R.fail(500, "The business is being processed, please try again later...");
    }

}

