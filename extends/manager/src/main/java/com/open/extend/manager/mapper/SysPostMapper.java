package com.open.extend.manager.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.open.extend.manager.domain.SysPost;
import com.open.extend.manager.domain.vo.SysPostVo;
import com.open.starter.mybatisplus.annotation.DataColumn;
import com.open.starter.mybatisplus.annotation.DataPermission;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author open
 */
public interface SysPostMapper extends IBaseMapper<SysPost, SysPostVo> {

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
