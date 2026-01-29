package com.open.extend.manager.function.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.open.common.core.constants.Constants;
import com.open.extend.manager.function.user.domain.enums.UserSex;
import com.open.extend.manager.function.user.domain.enums.UserStatus;
import com.open.starter.tenant.core.domain.TenantAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
     * 用户账号
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号前缀
     */
    private String mobilePrefix;
    /**
     * 手机号后缀
     */
    private String mobileSuffix;
    /**
     * 用户性别
     */
    private UserSex sex;
    /**
     * 用户头像
     */
    private String avatar;
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
     * 帐号状态
     */
    private UserStatus status;
    /**
     * 最后登录IP
     */
    private String loginIp;
    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 备注
     */
    private String remark;

    public boolean isSuperAdmin() {
        return Constants.SUPER_ADMIN_ID.equals(this.getId());
    }

    public String getMobile() {
        return "+" + mobilePrefix + " " + mobileSuffix;
    }

}
