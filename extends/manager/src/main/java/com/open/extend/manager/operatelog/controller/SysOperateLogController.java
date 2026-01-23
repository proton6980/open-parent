package com.open.extend.manager.operatelog.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;

import javax.servlet.http.HttpServletResponse;

import com.open.common.core.pojo.R;
import com.open.extend.manager.operatelog.domain.bo.SysOperateLogBo;
import com.open.extend.manager.operatelog.domain.vo.SysOperateLogVo;
import com.open.extend.manager.operatelog.service.ISysOperateLogService;
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
 * 管理后台/系统管理/操作日志记录
 *
 * @author open
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/operate/log")
public class SysOperateLogController extends BaseController {

    private final ISysOperateLogService operLogService;

    /**
     * 获取操作日志记录列表
     */
    @SaCheckPermission("monitor:operatelog:list")
    @GetMapping("/list")
    public TableDataInfo<SysOperateLogVo> list(SysOperateLogBo operLog, PageQuery pageQuery) {
        return operLogService.selectPageOperLogList(operLog, pageQuery);
    }

    /**
     * 导出操作日志记录列表
     */
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @SaCheckPermission("monitor:operatelog:export")
    @PostMapping("/export")
    public void export(SysOperateLogBo operLog, HttpServletResponse response) {
        List<SysOperateLogVo> list = operLogService.selectOperLogList(operLog);
        ExcelUtil.exportExcel(list, "操作日志", SysOperateLogVo.class, response);
    }

    /**
     * 批量删除操作日志记录
     *
     * @param operIds 日志ids
     */
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @SaCheckPermission("monitor:operatelog:remove")
    @DeleteMapping("/{operIds}")
    public R<Void> remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    /**
     * 清理操作日志记录
     */
    @SaCheckPermission("monitor:operatelog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<Void> clean() {
        operLogService.cleanOperLog();
        return R.ok();
    }
}
