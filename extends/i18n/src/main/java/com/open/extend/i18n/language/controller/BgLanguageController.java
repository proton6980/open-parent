package com.open.extend.i18n.language.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.open.common.core.pojo.validated.Save;
import com.open.common.core.pojo.validated.Update;
import com.open.extend.i18n.language.domain.bo.LanguageBo;
import com.open.extend.i18n.language.domain.vo.LanguageVo;
import com.open.extend.i18n.language.service.LanguageService;
import com.open.common.core.pojo.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台/多语言/语言控制器
 *
 * @author open
 */
@RestController
@RequestMapping("/bg/language")
@RequiredArgsConstructor
public class BgLanguageController {
    private final LanguageService languageService;

    /**
     * 分页列表
     *
     * @return 分页列表
     */
    @GetMapping("/page")
    public Page<LanguageVo> page(LanguageBo bo, PageQuery pageQuery) {
        return languageService.pageVo(pageQuery, bo);
    }

    /**
     * 添加
     *
     * @param bo 入参
     * @return 是否成功
     */
    @PostMapping
    public Boolean save(@RequestBody @Validated(Save.class) LanguageBo bo) {
        return languageService.save(bo);
    }

    /**
     * 添加
     *
     * @param bo 入参
     * @return 是否成功
     */
    @PutMapping
    public Boolean update(@RequestBody @Validated(Update.class) LanguageBo bo) {
        return languageService.update(bo);
    }

    /**
     * 删除
     *
     * @param ids 主键数组
     * @return 是否成功
     */
    @DeleteMapping("/{ids}")
    public boolean removeById(@PathVariable Long[] ids) {
        return languageService.removeById(ids);
    }
}
