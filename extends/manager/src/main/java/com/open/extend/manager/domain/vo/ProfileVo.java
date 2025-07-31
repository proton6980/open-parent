package com.open.extend.manager.domain.vo;

import com.open.extend.manager.user.domain.vo.SysUserVo;
import lombok.Data;

/**
 * 用户个人信息
 *
 * @author open
 */
@Data
public class ProfileVo {

    /**
     * 用户信息
     */
    private SysUserVo user;

    /**
     * 用户所属角色组
     */
    private String roleGroup;

    /**
     * 用户所属岗位组
     */
    private String postGroup;


}
