package com.open.extend.manager.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.open.common.core.pojo.R;
import com.open.common.business.service.IDictService;
import com.open.extend.manager.domain.bo.SysNoticeBo;
import com.open.extend.manager.domain.vo.SysNoticeVo;
import com.open.extend.manager.service.ISysNoticeService;
import com.open.common.core.pojo.page.PageQuery;
import com.open.common.core.pojo.page.TableDataInfo;
import com.open.starter.satoken.annotation.Log;
import com.open.starter.satoken.enums.BusinessType;
import com.open.starter.sse.utils.SseMessageUtils;
import com.open.starter.web.core.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 公告 信息操作处理
 *
 * @author open
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController {

    private final ISysNoticeService noticeService;
    private final IDictService dictService;

    /**
     * 获取通知公告列表
     */
    @SaCheckPermission("system:notice:list")
    @GetMapping("/list")
    public TableDataInfo<SysNoticeVo> list(SysNoticeBo notice, PageQuery pageQuery) {
        return noticeService.selectPageNoticeList(notice, pageQuery);
    }

    /**
     * 根据通知公告编号获取详细信息
     *
     * @param noticeId 公告ID
     */
    @SaCheckPermission("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public R<SysNoticeVo> getInfo(@PathVariable Long noticeId) {
        return R.ok(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @SaCheckPermission("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysNoticeBo notice) {
        int rows = noticeService.insertNotice(notice);
        if (rows <= 0) {
            return R.fail();
        }
        String type = dictService.getDictLabel("sys_notice_type", notice.getNoticeType());
        SseMessageUtils.publishAll("[" + type + "] " + notice.getNoticeTitle());
        return R.ok();
    }

    /**
     * 修改通知公告
     */
    @SaCheckPermission("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysNoticeBo notice) {
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     *
     * @param noticeIds 公告ID串
     */
    @SaCheckPermission("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public R<Void> remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
