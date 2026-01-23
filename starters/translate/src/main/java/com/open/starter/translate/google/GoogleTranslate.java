package com.open.starter.translate.google;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.open.common.core.utils.JacksonUtils;
import com.open.common.core.utils.OkHttpUtil;
import com.open.common.core.utils.StringUtils;
import com.open.common.core.utils.translate.ITranslateClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Map;

/**
 * google翻译
 *
 * @author open
 */
@Slf4j
public class GoogleTranslate implements ITranslateClient {

    private static final String TRANSLATE_URL = "https://translate.google.com/translate_a/single";

    @Override
    public String translate(Locale locale, String text) {
        try {
            String targetLang = locale.toLanguageTag();
            // 如果目标语言是中文，转换为zh-CN格式
            if (targetLang.startsWith("zh")) {
                targetLang = "zh-CN";
            }

            return translate2(text, "auto", targetLang);
        } catch (Exception e) {
            log.error("翻译出错", e);
            return text; // 翻译失败时返回原文本
        }
    }

    @Override
    public String translate(Locale locale, String text, Object... args) {
        return translate(locale, StringUtils.format(text, args));
    }

    /**
     * 翻译文本
     *
     * @param text       要翻译的文本
     * @param sourceLang 源语言代码，如 zh-CN, en 等，传 "auto" 表示自动检测
     * @param targetLang 目标语言代码，如 zh-CN, en 等
     * @return 翻译后的文本
     */
    public static String translate2(String text, String sourceLang, String targetLang) {
        try {
            // 构建查询参数字符串
            String queryParams = String.format("client=gtx&dt=t&dj=1&ie=UTF-8&sl=%s&tl=%s&q=%s", sourceLang, targetLang,
                    java.net.URLEncoder.encode(text, "UTF-8"));

            String urlWithParams = TRANSLATE_URL + "?" + queryParams;

            String result = OkHttpUtil.get(urlWithParams);

            if (StringUtils.isEmpty(result)) {
                log.error("谷歌翻译请求失败，返回结果为空");
            } else {
                // 解析返回的JSON数据
                return parseTranslationResult(result);
            }
        } catch (Exception e) {
            log.error("谷歌翻译出错", e);
        }
        return text; // 翻译失败时返回原文本
    }

    /**
     * 解析翻译结果
     *
     * @param jsonResult Google翻译API返回的JSON字符串
     * @return 翻译后的文本
     */
    private static String parseTranslationResult(String jsonResult) {
        try {
            // 解析JSON结果
            Map<String, Object> resultMap = JacksonUtils.parseMap(jsonResult);

            // 获取翻译句子数组
            Object sentencesObj = resultMap.get("sentences");
            if (sentencesObj instanceof Iterable) {
                StringBuilder translatedText = new StringBuilder();
                Iterable<?> sentences = (Iterable<?>) sentencesObj;

                for (Object sentenceObj : sentences) {
                    if (sentenceObj instanceof Map) {
                        Map<?, ?> sentenceMap = (Map<?, ?>) sentenceObj;
                        Object transObj = sentenceMap.get("trans");
                        if (transObj != null) {
                            translatedText.append(transObj.toString());
                        }
                    }
                }

                return translatedText.toString();
            }
        } catch (Exception e) {
            log.error("解析翻译结果出错", e);
        }
        return "";
    }

    /**
     * 检测文本语言
     *
     * @param text 要检测的文本
     * @return 检测到的语言代码
     */
    public static String detectLanguage(String text) {
        try {
            // 构建查询参数字符串
            String queryParams = String.format(
                    "client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=en&q=%s",
                    java.net.URLEncoder.encode(text, "UTF-8")
            );

            String urlWithParams = TRANSLATE_URL + "?" + queryParams;

            String result = OkHttpUtil.get(urlWithParams);

            if (StringUtils.isNotEmpty(result)) {
                // 解析返回的JSON数据获取源语言
                return parseSourceLanguage(result);
            }
        } catch (Exception e) {
            log.error("语言检测出错", e);
        }
        return "auto";
    }

    /**
     * 解析源语言
     *
     * @param jsonResult Google翻译API返回的JSON字符串
     * @return 源语言代码
     */
    private static String parseSourceLanguage(String jsonResult) {
        try {
            Map<String, Object> resultMap = JacksonUtils.parseMap(jsonResult);
            String src = MapUtil.getStr(resultMap, "src");
            if (StrUtil.isNotEmpty(src)) {
                return src;
            }
        } catch (Exception e) {
            log.error("解析源语言出错", e);
        }
        return "auto";
    }
}
