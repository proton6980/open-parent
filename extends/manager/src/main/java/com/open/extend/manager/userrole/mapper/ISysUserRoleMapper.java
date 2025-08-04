package com.open.extend.manager.userrole.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.userrole.domain.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysUserRoleMapper.class)
public interface ISysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
