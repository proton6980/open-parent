package com.open.extend.manager.function.loginlog.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统访问记录表 sys_login_log
 *
 * @author open
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog implements Serializable {

    /**
     * ID
     */
    @TableId(value = "info_id")
    private Long infoId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 客户端
     */
    private String clientKey;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 登录状态
     */
    private Boolean status;

    /**
     * 登录IP地址
     */
    private String loginIp;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private LocalDateTime loginTime;

}
