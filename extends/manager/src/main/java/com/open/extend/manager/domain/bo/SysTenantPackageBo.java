package com.open.extend.manager.domain.bo;

import com.open.commons.validated.Save;
import com.open.commons.validated.Update;
import com.open.extend.manager.domain.SysTenantPackage;
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
    @AutoMapping(target = "menuIds", expression = "java(com.open.commons.utils.StringUtils.join(source.getMenuIds(), \",\"))")
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
     * 状态（0正常 1停用）
     */
    private String status;
}
