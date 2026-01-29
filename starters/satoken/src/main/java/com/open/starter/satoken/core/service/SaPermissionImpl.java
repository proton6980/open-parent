package com.open.starter.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.open.common.business.enums.UserType;
import com.open.common.business.pojo.model.LoginUser;
import com.open.common.business.service.IPermissionService;
import com.open.common.core.utils.StringUtils;
import com.open.common.spring.exception.OpenBusinessException;
import com.open.starter.satoken.utils.LoginHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 *
 * @author open
 */
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (ObjectUtil.isNull(loginUser) || !loginUser.getLoginId().equals(loginId)) {
            IPermissionService permissionService = getPermissionService();
            if (ObjectUtil.isNotNull(permissionService)) {
                List<String> list = StringUtils.splitList(loginId.toString(), ":");
                return new ArrayList<>(permissionService.getMenuPermission(Long.parseLong(list.get(1))));
            } else {
                throw new OpenBusinessException("PermissionService implementation class does not exist");
            }
        }
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.APP_USER) {
            IPermissionService permissionService = getPermissionService();
            if (ObjectUtil.isNotNull(permissionService)) {
                return new ArrayList<>(permissionService.getMenuPermission(loginUser));
            }
            return new ArrayList<>();
        }
        // SYS_USER 默认返回权限
        return new ArrayList<>(loginUser.getMenuPermission());
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (ObjectUtil.isNull(loginUser) || !loginUser.getLoginId().equals(loginId)) {
            IPermissionService permissionService = getPermissionService();
            if (ObjectUtil.isNotNull(permissionService)) {
                List<String> list = StringUtils.splitList(loginId.toString(), ":");
                return new ArrayList<>(permissionService.getRolePermission(Long.parseLong(list.get(1))));
            } else {
                throw new OpenBusinessException("PermissionService implementation class does not exist");
            }
        }
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.APP_USER) {
            IPermissionService permissionService = getPermissionService();
            if (ObjectUtil.isNotNull(permissionService)) {
                return new ArrayList<>(permissionService.getRolePermission(loginUser));
            }
            return new ArrayList<>();
        }
        // SYS_USER 默认返回权限
        return new ArrayList<>(loginUser.getRolePermission());
    }

    private IPermissionService getPermissionService() {
        try {
            return SpringUtil.getBean(IPermissionService.class);
        } catch (Exception e) {
            return null;
        }
    }

}

