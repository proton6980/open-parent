package com.open.extend.manager.dict.mapper;

import com.open.extend.manager.dict.domain.SysDictType;
import com.open.extend.manager.dict.domain.vo.SysDictTypeVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 字典表 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysDictTypeMapper.class)
public interface ISysDictTypeMapper extends IBaseMapper<SysDictType, SysDictTypeVo> {

}
