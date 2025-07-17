package com.open.extend.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.domain.SysUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author godLian
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
