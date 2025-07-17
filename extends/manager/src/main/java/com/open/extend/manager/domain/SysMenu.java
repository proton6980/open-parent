package com.open.extend.manager.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.open.commons.constants.Constants;
import com.open.commons.utils.StringUtils;
import com.open.starter.mybatisplus.core.domain.BaseAdminEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限表 sys_menu
 *
 * @author godLian
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseAdminEntity {
    /**
     * 父菜单ID
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 路由参数
     */
    private String queryParam;
    /**
     * 是否为外链（0是 1否）
     */
    private String isFrame;
    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private String isCache;
    /**
     * 类型（M目录 C菜单 F按钮）
     */
    private String menuType;
    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;
    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;
    /**
     * 权限字符串
     */
    private String perms;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 备注
     */
    private String remark;
    /**
     * 父菜单名称
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

    /**
     * 获取路由名称
     */
    public String getRouteName() {
        String routerName = StringUtils.capitalize(path);
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame()) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     */
    public String getRouterPath() {
        String routerPath = this.path;
        // 内链打开外网方式
        if (getParentId() != 0L && isInnerLink()) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0L == getParentId() && Constants.TYPE_DIR.equals(getMenuType())
                && Constants.NO_FRAME.equals(getIsFrame())) {
            routerPath = "/" + this.path;
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame()) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     */
    public String getComponentInfo() {
        String component = Constants.LAYOUT;
        if (StringUtils.isNotEmpty(this.component) && !isMenuFrame()) {
            component = this.component;
        } else if (StringUtils.isEmpty(this.component) && getParentId() != 0L && isInnerLink()) {
            component = Constants.INNER_LINK;
        } else if (StringUtils.isEmpty(this.component) && isParentView()) {
            component = Constants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     */
    public boolean isMenuFrame() {
        return getParentId() == 0L && Constants.TYPE_MENU.equals(menuType) && isFrame.equals(Constants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     */
    public boolean isInnerLink() {
        return isFrame.equals(Constants.NO_FRAME) && StringUtils.ishttp(path);
    }

    /**
     * 是否为parent_view组件
     */
    public boolean isParentView() {
        return getParentId() != 0L && Constants.TYPE_DIR.equals(menuType);
    }

    /**
     * 内链域名特殊字符替换
     */
    public static String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }
}
