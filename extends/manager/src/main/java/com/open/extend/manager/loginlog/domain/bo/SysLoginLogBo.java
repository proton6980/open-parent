package com.open.extend.manager.loginlog.domain.bo;

import com.open.extend.manager.loginlog.domain.SysLoginLog;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统访问记录业务对象 sys_login_log
 *
 * @author open
 */
@Data
@AutoMapper(target = SysLoginLog.class, reverseConvertGenerate = false)
public class SysLoginLogBo {

    /**
     * 访问ID
     */
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
     * 登录状态
     */
    private Boolean status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private LocalDateTime loginTime;

    /**
     * 请求参数
     */
    private Map<String, Object> params = new HashMap<>();


}
