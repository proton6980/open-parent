package com.open.extend.manager.function.client.mapper;

import com.open.extend.manager.function.client.domain.SysClient;
import com.open.extend.manager.function.client.domain.vo.SysClientVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 授权管理Mapper接口
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysClientMapper.class)
public interface ISysClientMapper extends IBaseMapper<SysClient, SysClientVo> {

}
