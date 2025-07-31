package com.open.commons.utils;

import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 国际化工具类
 *
 * @author open
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class I18nUtils {

    /**
     * 获取当前语言
     *
     * @return 语言
     */
    public static String getLanguage() {
        Locale locale = LocaleContextHolder.getLocale();
        return String.format("%s-%s", locale.getLanguage(), locale.getCountry());
    }
}
