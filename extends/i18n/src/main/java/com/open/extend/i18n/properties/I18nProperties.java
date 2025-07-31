package com.open.extend.i18n.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * open i18n 配置项
 * @author open
 */
@Data
@ConfigurationProperties(prefix = "open.i18n")
public class I18nProperties {
    /**
     * 是否初始化sql
     */
    private Boolean initSql = false;

}
