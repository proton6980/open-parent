package com.open.extend.manager.loginlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.loginlog.domain.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysLoginLogMapper.class)
public interface ISysLoginLogMapper extends BaseMapper<SysLoginLog> {

}
