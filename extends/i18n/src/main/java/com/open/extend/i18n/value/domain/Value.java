package com.open.extend.i18n.value.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 值表
 *
 * @author open
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("i18n_value")
public class Value extends BaseAdminEntity {

    /**
     * 键ID
     */
    private Long keyId;
    /**
     * 语言ID
     */
    private Long languageId;
    /**
     * 内容
     */
    private String content;
    /**
     * 备注
     */
    private String remark;
}

