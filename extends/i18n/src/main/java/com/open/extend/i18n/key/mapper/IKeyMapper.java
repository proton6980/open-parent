package com.open.extend.i18n.key.mapper;

import com.open.extend.i18n.key.domain.Key;
import com.open.extend.i18n.key.domain.vo.KeyVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 键表数据库操作接口
 *
 * @author godLian
 */
@Mapper
public interface IKeyMapper extends IBaseMapper<Key, KeyVo> {
}
