package com.open.extend.manager.userpost.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.open.extend.manager.userpost.domain.SysUserPost;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * 用户与岗位关联表 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysUserPostMapper.class)
public interface ISysUserPostMapper extends BaseMapper<SysUserPost> {

}
