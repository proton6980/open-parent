package com.open.extend.i18n.value.domain.vo;

import com.open.extend.i18n.value.domain.Value;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;

/**
 * 值出参
 *
 * @author open
 */
@Data
@AutoMapper(target = Value.class)
public class ValueVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long keyId;

    private Long LanguageId;

    private String content;

    private String remark;
}
