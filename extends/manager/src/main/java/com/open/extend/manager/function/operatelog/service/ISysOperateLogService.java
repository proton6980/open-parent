package com.open.extend.manager.function.operatelog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.open.extend.manager.function.operatelog.domain.SysOperateLog;
import com.open.common.core.pojo.page.PageQuery;
import com.open.common.core.pojo.page.TableDataInfo;
import com.open.extend.manager.function.operatelog.domain.bo.SysOperateLogBo;
import com.open.extend.manager.function.operatelog.domain.vo.SysOperateLogVo;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author open
 */
public interface ISysOperateLogService extends IService<SysOperateLog> {

    TableDataInfo<SysOperateLogVo> selectPageOperLogList(SysOperateLogBo operLog, PageQuery pageQuery);

    /**
     * 新增操作日志
     *
     * @param bo 操作日志对象
     */
    void insertOperlog(SysOperateLogBo bo);

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    List<SysOperateLogVo> selectOperLogList(SysOperateLogBo operLog);

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    int deleteOperLogByIds(Long[] operIds);

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    SysOperateLogVo selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
