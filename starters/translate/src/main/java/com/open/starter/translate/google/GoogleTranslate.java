package com.open.starter.translate.google;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.open.commons.utils.JacksonUtils;
import com.open.commons.utils.OkHttpUtil;
import com.open.starter.translate.ITranslate;
import com.open.starter.translate.exception.OpenTranslateException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * google翻译
 *
 * @author open
 */
@Slf4j
public class GoogleTranslate implements ITranslate {

    @Override
    public String translate(String text, String from, String to) throws OpenTranslateException {
        try {
//            OkHttpUtil.get()


            return "";
        } catch (Exception e) {
//            throw new OpenTranslateException();
        }
        return "";
    }

    private static final String TRANSLATE_URL = "https://translate.google.com/translate_a/single";

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
            Map<String, Object> params = new HashMap<>();
            params.put("client", "gtx");
            params.put("dt", "t");
            params.put("dj", "1");
            params.put("ie", "UTF-8");
            params.put("sl", sourceLang);
            params.put("tl", targetLang);
            params.put("q", text);

            HttpResponse response = HttpRequest.get(TRANSLATE_URL)
                    .form(params)
                    .timeout(5000)
                    .execute();

            if (response.isOk()) {
                String result = response.body();
                // 解析返回的JSON数据
                return parseTranslationResult(result);
            } else {
                log.error("谷歌翻译请求失败，状态码: {}", response.getStatus());
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
     * 自动检测文本语言并翻译
     *
     * @param text       要翻译的文本
     * @param targetLang 目标语言代码
     * @return 翻译后的文本
     */
//    public static String autoTranslate(String text, String targetLang) {
//        return translate(text, "auto", targetLang);
//    }

    /**
     * 检测文本语言
     *
     * @param text 要检测的文本
     * @return 检测到的语言代码
     */
    public static String detectLanguage(String text) {
        Map<String, Object> params = new HashMap<>();
        params.put("client", "gtx");
        params.put("dt", "t");
        params.put("dj", "1");
        params.put("ie", "UTF-8");
        params.put("sl", "auto");
        params.put("tl", "en");
        params.put("q", text);
        try(HttpResponse response = HttpRequest.get(TRANSLATE_URL)
                .form(params)
                .timeout(5000)
                .execute()) {

            if (response.isOk()) {
                String result = response.body();
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
            if (StrUtil.isEmpty(src)) {
                return src;
            }
        } catch (Exception e) {
            log.error("解析源语言出错", e);
        }
        return "auto";
    }
}
