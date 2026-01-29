package com.open.extend.manager.function.dept.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.open.starter.tenant.core.domain.TenantAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门表 sys_dept
 *
 * @author open
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class SysDept extends TenantAdminEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 父部门ID
     */
    private Long parentId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 部门类别编码
     */
    private String deptCategory;
    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 负责人
     */
    private Long leader;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门状态
     */
    private Boolean enable;
    /**
     * 祖级列表
     */
    private String ancestors;
}
