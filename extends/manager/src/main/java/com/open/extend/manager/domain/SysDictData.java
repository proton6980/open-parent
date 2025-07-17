package com.open.extend.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.commons.constants.Constants;
import com.open.starter.tenant.core.domain.TenantAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据表 sys_dict_data
 *
 * @author godLian
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class SysDictData extends TenantAdminEntity {

    /**
     * 字典排序
     */
    private Integer dictSort;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 备注
     */
    private String remark;

    public boolean getDefault() {
        return Constants.YES.equals(this.isDefault);
    }

}
