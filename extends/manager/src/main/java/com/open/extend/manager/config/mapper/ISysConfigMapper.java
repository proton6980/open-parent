package com.open.extend.manager.config.mapper;

import com.open.extend.manager.config.domain.SysConfig;
import com.open.extend.manager.config.domain.vo.SysConfigVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 参数配置 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysConfigMapper.class)
public interface ISysConfigMapper extends IBaseMapper<SysConfig, SysConfigVo> {

}
