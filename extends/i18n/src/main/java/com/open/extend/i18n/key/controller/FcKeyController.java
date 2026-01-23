package com.open.extend.i18n.key.controller;

import cn.hutool.core.util.StrUtil;
import com.open.extend.i18n.key.service.KeyService;
import com.open.common.core.utils.I18nUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 客户端API/多语言
 *
 * @author open
 */
@RestController
@RequestMapping("/fc/key")
@RequiredArgsConstructor
public class FcKeyController {
    private final KeyService keyService;

    /**
     * 语言包
     */
    @GetMapping("/json")
    public Map<String, String> json(@RequestParam(required = false) String languageCode) {
        if (StrUtil.isBlank(languageCode)) {
            languageCode = I18nUtils.getLanguage();
        }
        return keyService.build(languageCode);
    }
}
