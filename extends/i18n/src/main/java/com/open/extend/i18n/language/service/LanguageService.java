package com.open.extend.i18n.language.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.open.extend.i18n.language.domain.Language;
import com.open.extend.i18n.language.domain.bo.LanguageBo;
import com.open.extend.i18n.language.domain.vo.LanguageVo;
import com.open.starter.mybatisplus.core.page.PageQuery;

/**
 * 语言业务接口
 *
 * @author open
 */
public interface LanguageService extends IService<Language> {
    /**
     * 分页查询
     *
     * @param pageQuery 分页参数
     * @param bo        查询参数
     * @return 分页数据
     */
    Page<LanguageVo> pageVo(PageQuery pageQuery, LanguageBo bo);

    /**
     * 保存
     *
     * @param bo 保存参数
     * @return 是否保存成功
     */
    Boolean save(LanguageBo bo);

    /**
     * 更新
     *
     * @param bo 更新参数
     * @return 是否更新成功
     */
    Boolean update(LanguageBo bo);
}
