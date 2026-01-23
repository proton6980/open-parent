package com.open.common.core.utils;

import com.open.common.core.utils.translate.ITranslateClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 国际化工具类
 *
 * @author open
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18nUtils {

    // 静态持有一个默认的 ITranslateClient 实例。
    private static ITranslateClient CLIENT = new ITranslateClient() {
        @Override
        public String translate(Locale target, String text) {
            return text;
        }

        @Override
        public String translate(Locale target, String text, Object... args) {
            return StringUtils.format(text, args);
        }
    };

    /**
     *
     * 初始化方法，用于从外部(如Spring容器)注入一个 ITranslateClient 实例。
     *
     * @param springTranslateClient 外部配置的 ITranslateClient
     */
    public static void init(ITranslateClient springTranslateClient) {
        if (springTranslateClient != null) {
            CLIENT = springTranslateClient;
        }
    }

    /**
     * 翻译
     * @param code 编码
     * @param args 参数
     * @return 翻译结果
     */
    public static String message(String code, Object... args){
        return CLIENT.translate(LocaleContextHolder.getLocale(), code, args);
    }

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
