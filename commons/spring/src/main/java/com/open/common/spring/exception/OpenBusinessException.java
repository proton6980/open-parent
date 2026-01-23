package com.open.common.spring.exception;

import com.open.common.core.exception.OpenException;
import com.open.common.core.utils.StringUtils;
import com.open.common.core.utils.translate.ITranslateClient;
import com.open.common.spring.utils.SpringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * open封装异常
 *
 * @author open
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OpenBusinessException extends OpenException {

    public OpenBusinessException(String module, Integer code, String key, Object... args) {
        super(module, code, SpringUtils.getBean(ITranslateClient.class).translate(LocaleContextHolder.getLocale(), key, args));
    }

    public OpenBusinessException(Integer code, String defaultMessage) {
        this(StringUtils.EMPTY, code, defaultMessage);
    }


    public OpenBusinessException(String defaultMessage) {
        this(500, defaultMessage);
    }
}
