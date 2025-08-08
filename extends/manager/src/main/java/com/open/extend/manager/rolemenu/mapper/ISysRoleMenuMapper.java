package com.open.extend.manager.rolemenu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.rolemenu.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 角色与菜单关联表 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysRoleMenuMapper.class)
public interface ISysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

}
