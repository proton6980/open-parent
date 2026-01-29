package com.open.extend.manager.exception;

/**
 * 验证码校验异常
 *
 * @author open
 */
public class CaptchaException extends UserException {

    public CaptchaException() {
        super("captcha.not.match");
    }
}
