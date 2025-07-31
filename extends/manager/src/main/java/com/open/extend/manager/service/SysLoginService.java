package com.open.extend.manager.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.open.commons.constants.Constants;
import com.open.commons.constants.GlobalConstants;
import com.open.commons.constants.TenantConstants;
import com.open.commons.enums.UserType;
import com.open.commons.exception.BaseException;
import com.open.commons.exception.OpenException;
import com.open.commons.pojo.model.LoginUser;
import com.open.commons.utils.MessageUtils;
import com.open.commons.utils.StringUtils;
import com.open.extend.manager.controller.bo.RegisterBody;
import com.open.extend.manager.domain.bo.SysSocialBo;
import com.open.extend.manager.domain.vo.SysSocialVo;
import com.open.extend.manager.domain.vo.SysTenantVo;
import com.open.extend.manager.properties.CaptchaProperties;
import com.open.extend.manager.properties.UserPasswordProperties;
import com.open.extend.manager.user.domain.SysUser;
import com.open.extend.manager.user.domain.bo.SysUserBo;
import com.open.extend.manager.user.service.ISysUserService;
import com.open.starter.cache.utils.RedisUtils;
import com.open.starter.satoken.event.LogininforEvent;
import com.open.starter.satoken.utils.LoginHelper;
import com.open.starter.tenant.utils.TenantHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysLoginService {
    private final ISysTenantService systemTenantService;
    private final ISysUserService sysUserService;
    private final ISysConfigService sysConfigService;
    private final ISysSocialService sysSocialService;

    @Autowired
    private UserPasswordProperties userPasswordProperties;
    @Autowired
    private final CaptchaProperties captchaProperties;

    /**
     * 绑定第三方用户
     *
     * @param authUserData 授权响应实体
     */
    @Lock4j
    public void socialRegister(AuthUser authUserData) {
        String authId = authUserData.getSource() + authUserData.getUuid();
        // 第三方用户信息
        SysSocialBo bo = BeanUtil.toBean(authUserData, SysSocialBo.class);
        BeanUtil.copyProperties(authUserData.getToken(), bo);
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        bo.setAuthId(authId);
        bo.setOpenId(authUserData.getUuid());
        bo.setUserName(authUserData.getUsername());
        bo.setNickName(authUserData.getNickname());
        List<SysSocialVo> checkList = sysSocialService.selectByAuthId(authId);
        if (CollUtil.isNotEmpty(checkList)) {
            throw new BaseException("此三方账号已经被绑定!");
        }
        // 查询是否已经绑定用户
        SysSocialBo params = new SysSocialBo();
        params.setUserId(userId);
        params.setSource(bo.getSource());
        List<SysSocialVo> list = sysSocialService.queryList(params);
        if (CollUtil.isEmpty(list)) {
            // 没有绑定用户, 新增用户信息
            sysSocialService.insertByBo(bo);
        } else {
            // 更新用户信息
            bo.setId(list.get(0).getId());
            sysSocialService.updateByBo(bo);
            // 如果要绑定的平台账号已经被绑定过了 是否抛异常自行决断
            // throw new ServiceException("此平台账号已经被绑定!");
        }
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            if (ObjectUtil.isNull(loginUser)) {
                return;
            }
            if (TenantHelper.isEnable() && LoginHelper.isSuperAdmin()) {
                // 超级管理员 登出清除动态租户
                TenantHelper.clearDynamic();
            }
            recordLogininfor(loginUser.getTenantId(), loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {
        } finally {
            try {
                StpUtil.logout();
            } catch (NotLoginException ignored) {
            }
        }
    }

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        String tenantId = registerBody.getTenantId();
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        // 校验用户类型是否存在
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaEnabled = captchaProperties.getEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(tenantId, username, registerBody.getCode(), registerBody.getUuid());
        }

        // 注册用户信息
        SysUserBo sysUserBo = new SysUserBo();
        sysUserBo.setTenantId(tenantId);
        sysUserBo.setUserName(username);
        sysUserBo.setNickName(username);
        sysUserBo.setPassword(BCrypt.hashpw(password));
        sysUserBo.setUserType(userType);

        boolean exist = TenantHelper.dynamic(tenantId, () -> {
            if (!("true".equals(sysConfigService.selectConfigByKey("sys.account.registerUser")))) {
                throw new BaseException("当前系统没有开启注册功能");
            }
            return sysUserService.exists(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUserName, sysUserBo.getUserName()));
        });
        if (exist) {
            throw new OpenException("", 500, "user.register.save.error", username);
        }

        boolean regFlag = sysUserService.registerUser(sysUserBo, tenantId);
        if (!regFlag) {
            throw new OpenException("", 500, "user.register.error");
        }
        recordLogininfor(tenantId, username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String tenantId, String username, String code, String uuid) {
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.blankToDefault(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLogininfor(tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new BaseException("验证码不存在");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLogininfor(tenantId, username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
            throw new BaseException("验证码不正确");
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    public void recordLogininfor(String tenantId, String username, String status, String message) {
        // 封装对象
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setTenantId(tenantId);
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        SpringUtil.getApplicationContext().publishEvent(logininforEvent);
    }

//    /**
//     * 登录校验
//     */
//    public void checkLogin(LoginType loginType, String tenantId, String username, Supplier<Boolean> supplier) {
//        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
//        String loginFail = Constants.LOGIN_FAIL;
//        Integer maxRetryCount = userPasswordProperties.getMaxRetryCount();
//        Integer lockTime = userPasswordProperties.getLockTime();
//
//        // 获取用户登录错误次数，默认为0 (可自定义限制策略 例如: key + username + ip)
//        int errorNumber = ObjectUtil.defaultIfNull(RedisUtils.getCacheObject(errorKey), 0);
//        // 锁定时间内登录 则踢出
//        if (errorNumber >= maxRetryCount) {
//            recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
//            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
//        }
//
//        if (supplier.get()) {
//            // 错误次数递增
//            errorNumber++;
//            RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
//            // 达到规定错误次数 则锁定登录
//            if (errorNumber >= maxRetryCount) {
//                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
//                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
//            } else {
//                // 未达到规定错误次数
//                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
//                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
//            }
//        }
//
//        // 登录成功 清空错误次数
//        RedisUtils.deleteObject(errorKey);
//    }

    /**
     * 校验租户
     *
     * @param tenantId 租户ID
     */
    public void checkTenant(String tenantId) {
        if (!TenantHelper.isEnable()) {
            return;
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new RuntimeException("tenant.number.not.blank");
        }
        if (TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
            return;
        }
        SysTenantVo tenant = systemTenantService.queryByTenantId(tenantId);
        if (ObjectUtil.isNull(tenant)) {
            log.info("登录租户：{} 不存在.", tenantId);
            throw new RuntimeException("tenant.not.exists");
        } else if (Constants.DISABLE.equals(tenant.getStatus())) {
            log.info("登录租户：{} 已被停用.", tenantId);
            throw new RuntimeException("tenant.blocked");
        } else if (ObjectUtil.isNotNull(tenant.getExpireTime())
                && new Date().after(tenant.getExpireTime())) {
            log.info("登录租户：{} 已超过有效期.", tenantId);
            throw new RuntimeException("tenant.expired");
        }
    }
}
