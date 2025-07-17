package com.open.extend.manager.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.open.extend.manager.domain.SysDept;
import com.open.extend.manager.domain.vo.SysDeptVo;
import com.open.starter.mybatisplus.annotation.DataColumn;
import com.open.starter.mybatisplus.annotation.DataPermission;
import com.open.starter.mybatisplus.core.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author godLian
 */
public interface SysDeptMapper extends IBaseMapper<SysDept, SysDeptVo> {

    /**
     * 查询部门管理数据
     *
     * @param queryWrapper 查询条件
     * @return 部门信息集合
     */
    @DataPermission({
            @DataColumn(key = "deptName", value = "dept_id")
    })
    List<SysDeptVo> selectDeptList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper);

    @DataPermission({
            @DataColumn(key = "deptName", value = "dept_id")
    })
    long countDeptById(Long deptId);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

}
