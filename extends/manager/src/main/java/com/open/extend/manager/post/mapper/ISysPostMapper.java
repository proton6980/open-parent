package com.open.extend.manager.post.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.open.extend.manager.post.domain.SysPost;
import com.open.extend.manager.post.domain.vo.SysPostVo;
import com.open.starter.mybatisplus.annotation.DataColumn;
import com.open.starter.mybatisplus.annotation.DataPermission;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author open
 */
@Mapper
@ConditionalOnMissingBean(ISysPostMapper.class)
public interface ISysPostMapper extends IBaseMapper<SysPost, SysPostVo> {

    @DataPermission({
            @DataColumn(key = "deptName", value = "dept_id"),
            @DataColumn(key = "userName", value = "create_by")
    })
    Page<SysPostVo> selectPagePostList(@Param("page") Page<SysPostVo> page, @Param(Constants.WRAPPER) Wrapper<SysPost> queryWrapper);

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<SysPostVo> selectPostsByUserId(Long userId);

}
