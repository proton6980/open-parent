package com.open.extend.manager.roledept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.roledept.domain.SysRoleDept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 角色与部门关联表 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysRoleDeptMapper.class)
public interface ISysRoleDeptMapper extends BaseMapper<SysRoleDept> {

}
