package com.open.extend.i18n.key.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.open.common.core.pojo.validated.Save;
import com.open.common.core.pojo.validated.Update;
import com.open.extend.i18n.key.domain.bo.KyeBo;
import com.open.extend.i18n.key.domain.vo.KeyVo;
import com.open.extend.i18n.key.service.KeyService;
import com.open.common.core.pojo.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台/多语言/键控制器
 *
 * @author open
 */
@RestController
@RequestMapping("/bg/key")
@RequiredArgsConstructor
public class BgKeyController {
    private final KeyService keyService;

    /**
     * 分页列表
     *
     * @return 分页列表
     */
    @GetMapping("/page")
    public Page<KeyVo> page(KyeBo bo, PageQuery pageQuery) {
        return keyService.pageVo(pageQuery, bo);
    }

    /**
     * 添加
     *
     * @param bo 入参
     * @return 是否成功
     */
    @PostMapping
    public Boolean save(@RequestBody @Validated(Save.class) KyeBo bo) {
        return keyService.save(bo);
    }

    /**
     * 更新
     *
     * @param bo 入参
     * @return 是否成功
     */
    @PutMapping
    public Boolean update(@RequestBody @Validated(Update.class) KyeBo bo) {
        return keyService.update(bo);
    }

    /**
     * 删除
     *
     * @param ids 主键数组
     * @return 是否成功
     */
    @DeleteMapping("/{ids}")
    public boolean removeById(@PathVariable Long[] ids) {
        return keyService.removeById(ids);
    }
}
