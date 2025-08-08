package com.open.extend.manager.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.open.commons.utils.StreamUtils;
import com.open.extend.manager.dept.domain.SysDept;
import com.open.extend.manager.roledept.domain.SysRoleDept;
import com.open.extend.manager.dept.mapper.ISysDeptMapper;
import com.open.extend.manager.roledept.mapper.ISysRoleDeptMapper;
import com.open.extend.manager.service.ISysDataScopeService;
import com.open.starter.mybatisplus.helper.DataBaseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据权限 实现
 * <p>
 * 注意: 此Service内不允许调用标注`数据权限`注解的方法
 * 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
 *
 * @author open
 */
@RequiredArgsConstructor
@Service("sdss")
public class SysDataScopeServiceImpl implements ISysDataScopeService {

    private final ISysRoleDeptMapper roleDeptMapper;
    private final ISysDeptMapper deptMapper;

    /**
     * 获取角色自定义权限
     *
     * @param roleId 角色Id
     * @return 部门Id组
     */
    @Override
    public String getRoleCustom(Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return "-1";
        }
        List<SysRoleDept> list = roleDeptMapper.selectList(
                new LambdaQueryWrapper<SysRoleDept>()
                        .select(SysRoleDept::getDeptId)
                        .eq(SysRoleDept::getRoleId, roleId));
        if (CollUtil.isNotEmpty(list)) {
            return StreamUtils.join(list, rd -> Convert.toStr(rd.getDeptId()));
        }
        return "-1";
    }

    /**
     * 获取部门及以下权限
     *
     * @param deptId 部门Id
     * @return 部门Id组
     */
    @Override
    public String getDeptAndChild(Long deptId) {
        if (ObjectUtil.isNull(deptId)) {
            return "-1";
        }
        List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptId)
                .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
        ids.add(deptId);
        if (CollUtil.isNotEmpty(ids)) {
            return StreamUtils.join(ids, Convert::toStr);
        }
        return "-1";
    }

}
