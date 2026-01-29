package com.open.extend.manager.function.tenantpackage.mapper;

import com.open.extend.manager.function.tenantpackage.domain.SysTenantPackage;
import com.open.extend.manager.function.tenantpackage.domain.vo.SysTenantPackageVo;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 租户套餐Mapper接口
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysTenantPackageMapper.class)
public interface ISysTenantPackageMapper extends IBaseMapper<SysTenantPackage, SysTenantPackageVo> {

}
