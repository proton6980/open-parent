package com.open.extend.manager.loginlog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.open.extend.manager.loginlog.domain.SysLoginLog;
import com.open.common.core.pojo.page.PageQuery;
import com.open.common.core.pojo.page.TableDataInfo;
import com.open.extend.manager.loginlog.domain.bo.SysLoginLogBo;
import com.open.extend.manager.loginlog.domain.vo.SysLoginLogVo;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层
 *
 * @author open
 */
public interface ISysLoginLogService extends IService<SysLoginLog> {

    /**
     * 分页列表
     *
     * @param logininfor 查询参数
     * @param pageQuery  分页参数
     * @return 列表
     */
    TableDataInfo<SysLoginLogVo> pageTableData(SysLoginLogBo logininfor, PageQuery pageQuery);

    /**
     * 新增系统登录日志
     *
     * @param bo 访问日志对象
     */
    void insertLogininfor(SysLoginLogBo bo);

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    List<SysLoginLogVo> selectLogininforList(SysLoginLogBo logininfor);

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    int deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空系统登录日志
     */
    void cleanLogininfor();
}
