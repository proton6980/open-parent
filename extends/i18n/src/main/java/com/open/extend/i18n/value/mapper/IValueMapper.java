package com.open.extend.i18n.value.mapper;

import com.open.extend.i18n.value.domain.Value;
import com.open.extend.i18n.value.domain.vo.ValueVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 值表数据库操作接口
 *
 * @author open
 */
@Mapper
public interface IValueMapper extends IBaseMapper<Value, ValueVo> {
}
