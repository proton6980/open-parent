package com.open.extend.i18n.language.domain.vo;

import com.open.extend.i18n.language.domain.Language;
import com.open.starter.mybatisplus.core.convert.Base64ImageConvertor;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;

import java.io.Serializable;

/**
 * 语言出参
 *
 * @author open
 */
@Data
@AutoMapper(target = Language.class, reverseConvertGenerate = false, uses = Base64ImageConvertor.class)
public class LanguageVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String code;

    private String name;

    @AutoMapping(qualifiedByName = "string2ByteArray")
    private String icon;

    private String remark;
}
