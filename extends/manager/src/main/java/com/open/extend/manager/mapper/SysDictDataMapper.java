package com.open.extend.manager.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.open.extend.manager.domain.SysDictData;
import com.open.extend.manager.domain.vo.SysDictDataVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author godLian
 */
public interface SysDictDataMapper extends IBaseMapper<SysDictData, SysDictDataVo> {

    default List<SysDictDataVo> selectDictDataByType(String dictType) {
        return selectVoList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort));
    }
}
