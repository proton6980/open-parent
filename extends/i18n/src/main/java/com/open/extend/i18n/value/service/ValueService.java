package com.open.extend.i18n.value.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.open.extend.i18n.value.domain.Value;
import com.open.extend.i18n.value.domain.bo.ValueBo;
import com.open.extend.i18n.value.domain.vo.ValueVo;
import com.open.common.core.pojo.page.PageQuery;

/**
 * 值业务接口
 *
 * @author open
 */
public interface ValueService extends IService<Value> {
    /**
     * 分页查询
     *
     * @param pageQuery 分页参数
     * @param bo        查询参数
     * @return 分页数据
     */
    Page<ValueVo> pageVo(PageQuery pageQuery, ValueBo bo);

    /**
     * 保存
     *
     * @param bo 保存参数
     * @return 是否保存成功
     */
    Boolean save(ValueBo bo);

    /**
     * 更新
     *
     * @param bo 更新参数
     * @return 是否更新成功
     */
    Boolean update(ValueBo bo);
}
