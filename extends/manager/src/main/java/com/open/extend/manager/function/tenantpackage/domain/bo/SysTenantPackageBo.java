package com.open.extend.manager.function.tenantpackage.domain.bo;

import com.open.common.core.pojo.validated.Save;
import com.open.common.core.pojo.validated.Update;
import com.open.extend.manager.function.tenantpackage.domain.SysTenantPackage;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;

import javax.validation.constraints.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户套餐业务对象 sys_tenant_package
 *
 * @author open
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysTenantPackage.class, reverseConvertGenerate = false)
public class SysTenantPackageBo extends BaseAdminEntity {

    /**
     * 租户套餐id
     */
    @NotNull(message = "租户套餐id不能为空", groups = {Update.class})
    private Long id;

    /**
     * 套餐名称
     */
    @NotBlank(message = "套餐名称不能为空", groups = {Save.class, Update.class})
    private String packageName;

    /**
     * 关联菜单id
     */
    @AutoMapping(target = "menuIds", expression = "java(com.open.common.core.utils.StringUtils.join(source.getMenuIds(), \",\"))")
    private Long[] menuIds;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单树选择项是否关联显示
     */
    private Boolean menuCheckStrictly;

    /**
     * 启用|禁用
     */
    private Boolean enable;
}
