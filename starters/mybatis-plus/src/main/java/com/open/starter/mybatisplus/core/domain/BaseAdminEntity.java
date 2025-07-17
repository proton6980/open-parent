package com.open.starter.mybatisplus.core.domain;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 管理后台基础实体
 *
 * @author godLian
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseAdminEntity extends BaseEntity {
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 部门ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long deptId;

    /**
     * 扩展请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        return MapUtil.isEmpty(params) ? MapUtil.newHashMap() : params;
    }
}
