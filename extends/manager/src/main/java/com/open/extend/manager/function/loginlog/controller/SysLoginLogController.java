package com.open.extend.manager.function.loginlog.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;

import javax.servlet.http.HttpServletResponse;

import com.open.common.core.constants.Constants;
import com.open.common.core.pojo.R;
import com.open.extend.manager.function.loginlog.domain.bo.SysLoginLogBo;
import com.open.extend.manager.function.loginlog.domain.vo.SysLoginLogVo;
import com.open.extend.manager.function.loginlog.service.ISysLoginLogService;
import com.open.starter.cache.utils.RedisUtils;
import com.open.starter.excel.utils.ExcelUtil;
import com.open.common.core.pojo.page.PageQuery;
import com.open.common.core.pojo.page.TableDataInfo;
import com.open.starter.satoken.annotation.Log;
import com.open.starter.satoken.enums.BusinessType;
import com.open.starter.web.core.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台/系统管理/系统访问记录
 *
 * @author open
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/logininfor")
public class SysLoginLogController extends BaseController {

    private final ISysLoginLogService sysLoginLogService;

    /**
     * 获取系统访问记录列表
     */
    @SaCheckPermission("monitor:bo:list")
    @GetMapping("/list")
    public TableDataInfo<SysLoginLogVo> list(SysLoginLogBo bo, PageQuery pageQuery) {
        return sysLoginLogService.pageTableData(bo, pageQuery);
    }

    /**
     * 导出系统访问记录列表
     */
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @SaCheckPermission("monitor:logininfor:export")
    @PostMapping("/export")
    public void export(SysLoginLogBo logininfor, HttpServletResponse response) {
        List<SysLoginLogVo> list = sysLoginLogService.selectLogininforList(logininfor);
        ExcelUtil.exportExcel(list, "登录日志", SysLoginLogVo.class, response);
    }

    /**
     * 批量删除登录日志
     *
     * @param infoIds 日志ids
     */
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public R<Void> remove(@PathVariable Long[] infoIds) {
        return toAjax(sysLoginLogService.deleteLogininforByIds(infoIds));
    }

    /**
     * 清理系统访问记录
     */
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<Void> clean() {
        sysLoginLogService.cleanLogininfor();
        return R.ok();
    }

    @SaCheckPermission("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public R<Void> unlock(@PathVariable("userName") String userName) {
        String loginName = Constants.PWD_ERR_CNT_KEY + userName;
        if (RedisUtils.hasKey(loginName)) {
            RedisUtils.deleteObject(loginName);
        }
        return R.ok();
    }

}
