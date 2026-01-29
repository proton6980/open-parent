package com.open.extend.manager.function.menu.domain.vo;

import com.open.extend.manager.function.menu.domain.SysMenu;
import com.open.extend.manager.function.menu.domain.enums.MenuType;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 菜单权限视图对象 sys_menu
 *
 * @author open
 */
@Data
@AutoMapper(target = SysMenu.class)
public class SysMenuVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 父菜单ID
     */
    private Long parentId;

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
     * 是否为外链
     */
    private Boolean isFrame;

    /**
     * 是否缓存
     */
    private Boolean isCache;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private MenuType menuType;

    /**
     * 显示状态
     */
    private Boolean visible;

    /**
     * 菜单状态
     */
    private Boolean enable;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 创建部门
     */
    private Long deptId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子菜单
     */
    private List<SysMenuVo> children = new ArrayList<>();

}
