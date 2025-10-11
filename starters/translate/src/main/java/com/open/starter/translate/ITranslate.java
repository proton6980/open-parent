package com.open.starter.translate;

import com.open.starter.translate.exception.OpenTranslateException;

/**
 * 翻译接口
 *
 * @author open
 */
public interface ITranslate {

    /**
     * 翻译
     *
     * @param text 文本
     * @param from 源语言
     * @param to   目标语言
     * @return 翻译结果
     * @throws OpenTranslateException 翻译异常
     */
    String translate(String text, String from, String to) throws OpenTranslateException;
}
