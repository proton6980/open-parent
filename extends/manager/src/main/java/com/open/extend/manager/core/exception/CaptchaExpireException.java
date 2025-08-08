package com.open.extend.manager.core.exception;

/**
 * 验证码过期异常
 *
 * @author open
 */
public class CaptchaExpireException extends UserException {

    public CaptchaExpireException() {
        super("captcha.expired");
    }
}
