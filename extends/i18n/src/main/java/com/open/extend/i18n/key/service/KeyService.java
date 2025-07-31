package com.open.extend.i18n.key.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.open.extend.i18n.key.domain.Key;
import com.open.extend.i18n.key.domain.bo.KyeBo;
import com.open.extend.i18n.key.domain.vo.KeyVo;
import com.open.starter.mybatisplus.core.page.PageQuery;

import java.util.Map;

/**
 * 键业务接口
 *
 * @author open
 */
public interface KeyService extends IService<Key> {
    /**
     * 分页查询
     *
     * @param pageQuery 分页参数
     * @param bo        查询参数
     * @return 分页数据
     */
    Page<KeyVo> pageVo(PageQuery pageQuery, KyeBo bo);

    /**
     * 保存
     *
     * @param bo 保存参数
     * @return 是否保存成功
     */
    Boolean save(KyeBo bo);

    /**
     * 更新
     *
     * @param bo 更新参数
     * @return 是否更新成功
     */
    Boolean update(KyeBo bo);

    /**
     * 获取语言包
     *
     * @param languageCode 语言
     * @return 语言包
     */
    Map<String, String> build(String languageCode);

    /**
     * 构建多语言键值
     *
     * @param languageCode 语言编码
     * @param keyCode      键编码
     * @param args         参数
     * @return 多语言键值
     */
    String build(String languageCode, String keyCode, Object... args);
}
