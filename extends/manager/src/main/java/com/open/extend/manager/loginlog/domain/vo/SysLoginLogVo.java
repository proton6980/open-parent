package com.open.extend.manager.loginlog.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.open.extend.manager.loginlog.domain.SysLoginLog;
import com.open.starter.excel.annotation.ExcelDictFormat;
import com.open.starter.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统访问记录视图对象 sys_login_log
 *
 * @author open
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysLoginLog.class)
public class SysLoginLogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 访问ID
     */
    @ExcelProperty(value = "序号")
    private Long infoId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 用户账号
     */
    @ExcelProperty(value = "用户账号")
    private String username;

    /**
     * 客户端
     */
    @ExcelProperty(value = "客户端")
    private String clientKey;

    /**
     * 设备类型
     */
    @ExcelProperty(value = "设备类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_device_type")
    private String deviceType;

    /**
     * 登录状态
     */
    @ExcelProperty(value = "登录状态", converter = ExcelDictConvert.class)
    private Boolean status;

    /**
     * 登录IP地址
     */
    @ExcelProperty(value = "登录地址")
    private String loginIp;

    /**
     * 登录地点
     */
    @ExcelProperty(value = "登录地点")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @ExcelProperty(value = "浏览器")
    private String browser;

    /**
     * 操作系统
     */
    @ExcelProperty(value = "操作系统")
    private String os;


    /**
     * 提示消息
     */
    @ExcelProperty(value = "提示消息")
    private String msg;

    /**
     * 访问时间
     */
    @ExcelProperty(value = "访问时间")
    private LocalDateTime loginTime;


}
