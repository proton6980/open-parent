package com.open.extend.manager.function.tenantpackage.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户套餐对象 sys_tenant_package
 *
 * @author open
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_package")
public class SysTenantPackage extends BaseAdminEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 套餐名称
     */
    private String packageName;
    /**
     * 关联菜单id
     */
    private String menuIds;
    /**
     * 备注
     */
    private String remark;
    /**
     * 菜单树选择项是否关联显示【0=父子不互相关联显示;1=父子互相关联显示】
     */
    private Boolean menuCheckStrictly;
    /**
     * 启用|禁用
     */
    private Boolean enable;

}
