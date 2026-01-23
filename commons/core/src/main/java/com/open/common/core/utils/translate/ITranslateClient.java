package com.open.common.core.utils.translate;

import java.util.Locale;

/**
 * 翻译客户端
 *
 * @author open
 */
public interface ITranslateClient {
    /**
     * 翻译
     *
     * @param target 目标语言
     * @param text   文本
     * @return 翻译后文本
     */
    String translate(Locale target, String text);

    /**
     * 翻译
     *
     * @param target 目标语言
     * @param text   文本
     * @param args   文本参数
     * @return 翻译后文本
     */
    String translate(Locale target, String text, Object... args);
}
