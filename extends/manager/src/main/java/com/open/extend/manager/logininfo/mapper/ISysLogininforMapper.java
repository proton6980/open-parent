package com.open.extend.manager.logininfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.logininfo.domain.SysLogininfor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author open
 */
@ConditionalOnMissingBean(ISysLogininforMapper.class)
public interface ISysLogininforMapper extends BaseMapper<SysLogininfor> {

}
