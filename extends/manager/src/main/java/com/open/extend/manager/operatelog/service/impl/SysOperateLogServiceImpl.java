package com.open.extend.manager.operatelog.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.open.commons.utils.MapstructUtils;
import com.open.commons.utils.StringUtils;
import com.open.commons.utils.ip.AddressUtils;
import com.open.commons.pojo.page.PageQuery;
import com.open.commons.pojo.page.TableDataInfo;
import com.open.extend.manager.operatelog.domain.SysOperateLog;
import com.open.extend.manager.operatelog.domain.bo.SysOperateLogBo;
import com.open.extend.manager.operatelog.domain.vo.SysOperateLogVo;
import com.open.extend.manager.operatelog.mapper.ISysOperateLogMapper;
import com.open.extend.manager.operatelog.service.ISysOperateLogService;
import com.open.starter.satoken.event.OperateLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作日志 服务层处理
 *
 * @author open
 */
@RequiredArgsConstructor
@Service
public class SysOperateLogServiceImpl extends ServiceImpl<ISysOperateLogMapper, SysOperateLog> implements ISysOperateLogService {

    /**
     * 操作日志记录
     *
     * @param operLogEvent 操作日志事件
     */
    @EventListener
    public void recordOperate(OperateLogEvent operLogEvent) {
        SysOperateLogBo operLog = MapstructUtils.convert(operLogEvent, SysOperateLogBo.class);
        // 远程查询操作地点
        operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
        insertOperlog(operLog);
    }

    @Override
    public TableDataInfo<SysOperateLogVo> selectPageOperLogList(SysOperateLogBo operLog, PageQuery pageQuery) {
        Map<String, Object> params = operLog.getParams();
        LambdaQueryWrapper<SysOperateLog> lqw = new LambdaQueryWrapper<SysOperateLog>()
                .like(StringUtils.isNotBlank(operLog.getOperIp()), SysOperateLog::getOperIp, operLog.getOperIp())
                .like(StringUtils.isNotBlank(operLog.getTitle()), SysOperateLog::getTitle, operLog.getTitle())
                .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                        SysOperateLog::getBusinessType, operLog.getBusinessType())
                .func(f -> {
                    if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                        f.in(SysOperateLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                    }
                })
                .eq(operLog.getStatus() != null,
                        SysOperateLog::getStatus, operLog.getStatus())
                .like(StringUtils.isNotBlank(operLog.getOperName()), SysOperateLog::getOperName, operLog.getOperName())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysOperateLog::getOperTime, params.get("beginTime"), params.get("endTime"));
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("oper_id");
            pageQuery.setIsAsc("desc");
        }
        Page<SysOperateLog> page = baseMapper.selectPage(pageQuery.build(), lqw);
        List<SysOperateLogVo> result = page.getRecords().stream().map(e -> MapstructUtils.convert(e, SysOperateLogVo.class)).collect(Collectors.toList());
        return new TableDataInfo<>(result, page.getTotal());
    }

    /**
     * 新增操作日志
     *
     * @param bo 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperateLogBo bo) {
        SysOperateLog operLog = MapstructUtils.convert(bo, SysOperateLog.class);
        operLog.setOperTime(LocalDateTime.now());
        baseMapper.insert(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperateLogVo> selectOperLogList(SysOperateLogBo operLog) {
        Map<String, Object> params = operLog.getParams();
        return baseMapper.selectList(new LambdaQueryWrapper<SysOperateLog>()
                        .like(StringUtils.isNotBlank(operLog.getOperIp()), SysOperateLog::getOperIp, operLog.getOperIp())
                        .like(StringUtils.isNotBlank(operLog.getTitle()), SysOperateLog::getTitle, operLog.getTitle())
                        .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                                SysOperateLog::getBusinessType, operLog.getBusinessType())
                        .func(f -> {
                            if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                                f.in(SysOperateLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                            }
                        })
                        .eq(operLog.getStatus() != null && operLog.getStatus() > 0,
                                SysOperateLog::getStatus, operLog.getStatus())
                        .like(StringUtils.isNotBlank(operLog.getOperName()), SysOperateLog::getOperName, operLog.getOperName())
                        .between(params.get("beginTime") != null && params.get("endTime") != null,
                                SysOperateLog::getOperTime, params.get("beginTime"), params.get("endTime"))
                        .orderByDesc(SysOperateLog::getOperId))
                .stream().map(e -> MapstructUtils.convert(e, SysOperateLogVo.class)).collect(Collectors.toList());
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        return baseMapper.deleteByIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperateLogVo selectOperLogById(Long operId) {
        SysOperateLog sysOperLog = baseMapper.selectById(operId);
        return MapstructUtils.convert(sysOperLog, SysOperateLogVo.class);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        baseMapper.delete(new LambdaQueryWrapper<>());
    }
}
