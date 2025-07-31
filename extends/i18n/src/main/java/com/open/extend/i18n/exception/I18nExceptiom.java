package com.open.extend.i18n.exception;

import cn.hutool.extra.spring.SpringUtil;
import com.open.commons.exception.OpenException;
import com.open.commons.utils.I18nUtils;
import com.open.extend.i18n.key.service.KeyService;

/**
 * 多语言异常
 *
 * @author open
 */
public class I18nExceptiom extends OpenException {
    private final static String MODULE = "i18n-db";
    private final static KeyService keyService = SpringUtil.getBean(KeyService.class);
    private final static String language = I18nUtils.getLanguage();

    public I18nExceptiom(int code, String message) {
        super(MODULE, code, keyService.build(language, message));
    }

    public I18nExceptiom(int code, String message, Object... args) {
        super(MODULE, code, keyService.build(language, message, args));
    }
}
