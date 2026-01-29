package com.open.common.business.service;

import com.open.common.business.pojo.model.LoginUser;

import java.util.Set;

/**
 * 用户权限处理
 *
 * @author open
 */
public interface IPermissionService {

    /**
     * 获取角色数据权限
     *
     * @param userId 用户id
     * @return 角色权限信息
     */
    Set<String> getRolePermission(Long userId);

    /**
     * 获取菜单数据权限
     *
     * @param userId 用户id
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(Long userId);

    /**
     * 根据登录用户信息获取菜单权限
     *
     * @param loginUser 登录用户信息
     * @return 菜单权限信息
     */
    default Set<String> getMenuPermission(LoginUser loginUser) {
        return getMenuPermission(loginUser.getUserId());
    }

    /**
     * 根据登录用户信息获取角色权限
     *
     * @param loginUser 登录用户信息
     * @return 角色权限信息
     */
    default Set<String> getRolePermission(LoginUser loginUser) {
        return getRolePermission(loginUser.getUserId());
    }
}

