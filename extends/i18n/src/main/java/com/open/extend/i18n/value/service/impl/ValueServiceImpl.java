package com.open.extend.i18n.value.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.open.commons.utils.MapstructUtils;
import com.open.extend.i18n.value.domain.Value;
import com.open.extend.i18n.value.domain.bo.ValueBo;
import com.open.extend.i18n.value.domain.vo.ValueVo;
import com.open.extend.i18n.value.mapper.IValueMapper;
import com.open.extend.i18n.value.service.ValueService;
import com.open.starter.mybatisplus.core.page.PageQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * 值业务实现
 *
 * @author godLian
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(ValueService.class)
public class ValueServiceImpl extends ServiceImpl<IValueMapper, Value> implements ValueService {

    @Override
    public Page<ValueVo> pageVo(PageQuery pageQuery, ValueBo bo) {
        return this.baseMapper.selectVoPage(pageQuery.build(), Wrappers.lambdaQuery(Value.class));
    }

    @Override
    public Boolean save(ValueBo bo) {
        Value entity = MapstructUtils.convert(bo, Value.class);
        return this.save(entity);
    }

    @Override
    public Boolean update(ValueBo bo) {
        Value oldEntity = this.getById(bo.getId());
        Value newEntity = MapstructUtils.convert(bo, oldEntity);
        return this.updateById(newEntity);
    }
}
