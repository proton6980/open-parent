package com.open.extend.manager.function.role.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.starter.tenant.core.domain.TenantAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色表 sys_role
 *
 * @author open
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends TenantAdminEntity {
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色权限
     */
    private String roleKey;
    /**
     * 角色排序
     */
    private Integer roleSort;
    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    private String dataScope;
    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private Boolean menuCheckStrictly;
    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    private Boolean deptCheckStrictly;
    /**
     * 角色状态
     */
    private Boolean enable;
    /**
     * 备注
     */
    private String remark;
}
