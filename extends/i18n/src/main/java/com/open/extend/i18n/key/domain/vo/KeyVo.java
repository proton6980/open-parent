package com.open.extend.i18n.key.domain.vo;

import com.open.extend.i18n.key.domain.Key;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;

/**
 * 键出参
 *
 * @author open
 */
@Data
@AutoMapper(target = Key.class)
public class KeyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String code;

    private String name;

    private String remark;
}
