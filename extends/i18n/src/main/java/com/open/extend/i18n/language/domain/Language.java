package com.open.extend.i18n.language.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.extend.i18n.language.domain.vo.LanguageVo;
import com.open.starter.mybatisplus.core.convert.Base64ImageConvertor;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 语言表
 *
 * @author godLian
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("i18n_language")
@AutoMapper(target = LanguageVo.class, reverseConvertGenerate = false, uses = Base64ImageConvertor.class)
public class Language extends BaseAdminEntity {

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    @AutoMapping(qualifiedByName = "byteArray2String")
    private byte[] icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 区域标识
     */
    private Boolean regionFlag;
}

