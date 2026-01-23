package com.open.extend.manager.loginlog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.open.common.core.constants.Constants;
import com.open.common.core.utils.MapstructUtils;
import com.open.common.core.utils.ServletUtils;
import com.open.common.core.utils.StringUtils;
import com.open.common.core.utils.ip.AddressUtils;
import com.open.extend.manager.loginlog.domain.SysLoginLog;
import com.open.common.core.pojo.page.PageQuery;
import com.open.common.core.pojo.page.TableDataInfo;
import com.open.extend.manager.loginlog.domain.bo.SysLoginLogBo;
import com.open.extend.manager.client.domain.vo.SysClientVo;
import com.open.extend.manager.loginlog.domain.vo.SysLoginLogVo;
import com.open.extend.manager.loginlog.mapper.ISysLoginLogMapper;
import com.open.extend.manager.client.service.ISysClientService;
import com.open.extend.manager.loginlog.service.ISysLoginLogService;

import javax.servlet.http.HttpServletRequest;

import com.open.starter.satoken.event.LoginInfoEvent;
import com.open.starter.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author open
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl extends ServiceImpl<ISysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {
    private final ISysClientService clientService;

    /**
     * 记录登录信息
     *
     * @param loginInfoEvent 登录事件
     */
    @EventListener
    public void recordLoginInfo(LoginInfoEvent loginInfoEvent) {
        HttpServletRequest request = ServletUtils.getRequest();
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        final String ip = ServletUtils.getClientIP(request);
        // 客户端信息
        String clientId = request.getHeader(LoginHelper.CLIENT_KEY);
        SysClientVo client = null;
        if (StringUtils.isNotBlank(clientId)) {
            client = clientService.queryByClientId(clientId);
        }

        String address = AddressUtils.getRealAddressByIP(ip);
        StringBuilder s = new StringBuilder();
        s.append(getBlock(ip));
        s.append(address);
        s.append(getBlock(loginInfoEvent.getUsername()));
        s.append(getBlock(loginInfoEvent.getStatus()));
        s.append(getBlock(loginInfoEvent.getMessage()));
        // 打印信息到日志
        log.info(s.toString(), loginInfoEvent.getArgs());
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        SysLoginLogBo loginLogBo = new SysLoginLogBo();
        loginLogBo.setTenantId(loginInfoEvent.getTenantId());
        loginLogBo.setUsername(loginInfoEvent.getUsername());
        if (ObjectUtil.isNotNull(client)) {
            loginLogBo.setClientKey(client.getClientKey());
            loginLogBo.setDeviceType(client.getDeviceType());
        }
        loginLogBo.setLoginIp(ip);
        loginLogBo.setLoginLocation(address);
        loginLogBo.setBrowser(browser);
        loginLogBo.setOs(os);
        loginLogBo.setMsg(loginInfoEvent.getMessage());
        // 日志状态
        if (StringUtils.equalsAny(loginInfoEvent.getStatus(), Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginLogBo.setStatus(true);
        } else if (Constants.LOGIN_FAIL.equals(loginInfoEvent.getStatus())) {
            loginLogBo.setStatus(false);
        }
        // 插入数据
        insertLogininfor(loginLogBo);
    }

    private String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }

    @Override
    public TableDataInfo<SysLoginLogVo> pageTableData(SysLoginLogBo loginLogBo, PageQuery pageQuery) {
        Map<String, Object> params = loginLogBo.getParams();
        LambdaQueryWrapper<SysLoginLog> lqw = new LambdaQueryWrapper<SysLoginLog>()
                .like(StringUtils.isNotBlank(loginLogBo.getLoginIp()), SysLoginLog::getLoginIp, loginLogBo.getLoginIp())
                .eq(Objects.nonNull(loginLogBo.getStatus()), SysLoginLog::getStatus, loginLogBo.getStatus())
                .like(StringUtils.isNotBlank(loginLogBo.getUsername()), SysLoginLog::getUsername, loginLogBo.getUsername())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysLoginLog::getLoginTime, params.get("beginTime"), params.get("endTime"));
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("info_id");
            pageQuery.setIsAsc("desc");
        }
        Page<SysLoginLog> page = baseMapper.selectPage(pageQuery.build(), lqw);
        List<SysLoginLogVo> result = page.getRecords().stream().map(e -> MapstructUtils.convert(e, SysLoginLogVo.class)).collect(Collectors.toList());
        return new TableDataInfo<>(result, page.getTotal());
    }

    /**
     * 新增系统登录日志
     *
     * @param bo 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLoginLogBo bo) {
        SysLoginLog loginLog = MapstructUtils.convert(bo, SysLoginLog.class);
        loginLog.setLoginTime(LocalDateTime.now());
        baseMapper.insert(loginLog);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLoginLogVo> selectLogininforList(SysLoginLogBo logininfor) {
        Map<String, Object> params = logininfor.getParams();
        return baseMapper.selectList(new LambdaQueryWrapper<SysLoginLog>()
                        .like(StringUtils.isNotBlank(logininfor.getLoginIp()), SysLoginLog::getLoginIp, logininfor.getLoginIp())
                        .eq(Objects.nonNull(logininfor.getStatus()), SysLoginLog::getStatus, logininfor.getStatus())
                        .like(StringUtils.isNotBlank(logininfor.getUsername()), SysLoginLog::getUsername, logininfor.getUsername())
                        .between(params.get("beginTime") != null && params.get("endTime") != null,
                                SysLoginLog::getLoginTime, params.get("beginTime"), params.get("endTime"))
                        .orderByDesc(SysLoginLog::getInfoId))
                .stream().map(e -> MapstructUtils.convert(e, SysLoginLogVo.class)).collect(Collectors.toList());
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        return baseMapper.deleteByIds(Arrays.asList(infoIds));
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        baseMapper.delete(new LambdaQueryWrapper<>());
    }
}
