package com.open.extend.manager.dict.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.open.extend.manager.dict.domain.SysDictData;
import com.open.extend.manager.dict.domain.vo.SysDictDataVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysDictDataMapper.class)
public interface ISysDictDataMapper extends IBaseMapper<SysDictData, SysDictDataVo> {

    default List<SysDictDataVo> selectDictDataByType(String dictType) {
        return selectVoList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort));
    }
}
