package com.open.extend.manager.operatelog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.operatelog.domain.SysOperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 操作日志 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysOperateLogMapper.class)
public interface ISysOperateLogMapper extends BaseMapper<SysOperateLog> {

}
