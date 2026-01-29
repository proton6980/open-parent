package com.open.extend.manager.function.tenant.mapper;

import com.open.extend.manager.function.tenant.domain.SysTenant;
import com.open.extend.manager.function.tenant.domain.vo.SysTenantVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 租户Mapper接口
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(SysTenantMapper.class)
public interface SysTenantMapper extends IBaseMapper<SysTenant, SysTenantVo> {

}
