package com.open.extend.i18n.value.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.open.common.core.pojo.validated.Save;
import com.open.common.core.pojo.validated.Update;
import com.open.extend.i18n.value.domain.bo.ValueBo;
import com.open.extend.i18n.value.domain.vo.ValueVo;
import com.open.extend.i18n.value.service.ValueService;
import com.open.common.core.pojo.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台/多语言/值控制器
 * @author open
 */
@RestController
@RequestMapping("/bg/value")
@RequiredArgsConstructor
public class BgValueController {
    private final ValueService valueService;

    /**
     * 分页列表
     *
     * @return 分页列表
     */
    @GetMapping("/page")
    public Page<ValueVo> page(ValueBo bo, PageQuery pageQuery) {
        return valueService.pageVo(pageQuery, bo);
    }

    /**
     * 添加
     *
     * @param bo 入参
     * @return 是否成功
     */
    @PostMapping
    public Boolean save(@RequestBody @Validated(Save.class) ValueBo bo) {
        return valueService.save(bo);
    }

    /**
     * 更新
     *
     * @param bo 入参
     * @return 是否成功
     */
    @PutMapping
    public Boolean update(@RequestBody @Validated(Update.class) ValueBo bo) {
        return valueService.update(bo);
    }

    /**
     * 删除
     *
     * @param ids 主键数组
     * @return 是否成功
     */
    @DeleteMapping("/{ids}")
    public boolean removeById(@PathVariable Long[] ids) {
        return valueService.removeById(ids);
    }
}
