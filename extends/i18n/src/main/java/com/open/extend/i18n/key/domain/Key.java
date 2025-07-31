package com.open.extend.i18n.key.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 键表
 *
 * @author open
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("i18n_key")
public class Key extends BaseAdminEntity {

    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
}

