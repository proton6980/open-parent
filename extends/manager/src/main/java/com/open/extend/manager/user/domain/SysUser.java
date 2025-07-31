package com.open.extend.manager.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.open.commons.constants.Constants;
import com.open.starter.tenant.core.domain.TenantAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户对象 sys_user
 *
 * @author open
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends TenantAdminEntity {
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 用户账号
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String phonenumber;
    /**
     * 用户性别
     */
    private String sex;
    /**
     * 用户头像
     */
    private Long avatar;
    /**
     * 密码
     */
    @TableField(
            insertStrategy = FieldStrategy.NOT_EMPTY,
            updateStrategy = FieldStrategy.NOT_EMPTY,
            whereStrategy = FieldStrategy.NOT_EMPTY
    )
    private String password;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private Date loginDate;
    /**
     * 备注
     */
    private String remark;

    public boolean isSuperAdmin() {
        return Constants.SUPER_ADMIN_ID.equals(this.getId());
    }

}
