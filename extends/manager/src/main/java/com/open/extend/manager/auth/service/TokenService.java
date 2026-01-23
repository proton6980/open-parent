package com.open.extend.manager.auth.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.open.common.core.constants.Constants;
import com.open.common.core.constants.GlobalConstants;
import com.open.common.business.constants.TenantConstants;
import com.open.common.core.enums.CaptchaType;
import com.open.common.business.enums.LoginType;
import com.open.common.business.enums.UserType;
import com.open.common.core.utils.I18nUtils;
import com.open.common.core.utils.translate.ITranslateClient;
import com.open.common.spring.exception.OpenBusinessException;
import com.open.common.business.pojo.model.LoginUser;
import com.open.common.business.pojo.model.PostDTO;
import com.open.common.business.pojo.model.RoleDTO;
import com.open.common.core.utils.ReflectUtils;
import com.open.common.core.utils.StringUtils;
import com.open.common.spring.utils.SpringUtils;
import com.open.extend.manager.auth.bo.RegisterBody;
import com.open.extend.manager.auth.vo.CaptchaVo;
import com.open.extend.manager.config.service.ISysConfigService;
import com.open.extend.manager.dept.domain.vo.SysDeptVo;
import com.open.extend.manager.dept.service.ISysDeptService;
import com.open.extend.manager.domain.bo.SysSocialBo;
import com.open.extend.manager.domain.vo.*;
import com.open.extend.manager.core.exception.UserException;
import com.open.extend.manager.post.domain.vo.SysPostVo;
import com.open.extend.manager.post.service.ISysPostService;
import com.open.extend.manager.core.properties.CaptchaProperties;
import com.open.extend.manager.core.properties.UserPasswordProperties;
import com.open.extend.manager.role.domain.vo.SysRoleVo;
import com.open.extend.manager.role.service.ISysRoleService;
import com.open.extend.manager.service.*;
import com.open.extend.manager.tenant.domain.vo.SysTenantVo;
import com.open.extend.manager.tenant.service.ISysTenantService;
import com.open.extend.manager.user.domain.SysUser;
import com.open.extend.manager.user.domain.bo.SysUserBo;
import com.open.extend.manager.user.service.ISysUserService;
import com.open.starter.cache.annotation.RateLimiter;
import com.open.starter.cache.enums.LimitType;
import com.open.starter.cache.utils.RedisUtils;
import com.open.starter.satoken.event.LoginInfoEvent;
import com.open.starter.satoken.utils.LoginHelper;
import com.open.starter.tenant.utils.TenantHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author open
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TokenService {
    private final ISysTenantService systemTenantService;
    private final ISysUserService sysUserService;
    private final ISysPermissionService sysPermissionService;
    private final ISysDeptService sysDeptService;
    private final ISysRoleService sysRoleService;
    private final ISysPostService sysPostService;
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
            throw new OpenBusinessException("此三方账号已经被绑定!");
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
            recordLoginInfo(loginUser.getTenantId(), loginUser.getUsername(), Constants.LOGOUT,
                    SpringUtils.getBean(ITranslateClient.class).translate(LocaleContextHolder.getLocale(), "user.logout.success"));
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
                throw new OpenBusinessException("当前系统没有开启注册功能");
            }
            return sysUserService.exists(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, sysUserBo.getUserName()));
        });
        if (exist) {
            throw new OpenBusinessException("", 500, "user.register.save.error", username);
        }

        boolean regFlag = sysUserService.registerUser(sysUserBo, tenantId);
        if (!regFlag) {
            throw new OpenBusinessException("user.register.error");
        }
        recordLoginInfo(tenantId, username, Constants.REGISTER, I18nUtils.message("user.register.success"));
    }

    /**
     * 生成验证码
     * 独立方法避免验证码关闭之后仍然走限流
     */
    @RateLimiter(count = 10, limitType = LimitType.IP)
    public CaptchaVo getCodeImpl() {
        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtil.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        // 如果是数学验证码，使用SpEL表达式处理验证码结果
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));
        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setUuid(uuid);
        captchaVo.setImg(captcha.getImageBase64());
        return captchaVo;
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
            recordLoginInfo(tenantId, username, Constants.LOGIN_FAIL, I18nUtils.message("user.jcaptcha.expire"));
            throw new OpenBusinessException("验证码不存在");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLoginInfo(tenantId, username, Constants.LOGIN_FAIL, I18nUtils.message("user.jcaptcha.error"));
            throw new OpenBusinessException("验证码不正确");
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
    public void recordLoginInfo(String tenantId, String username, String status, String message) {
        // 封装对象
        LoginInfoEvent loginInfoEvent = new LoginInfoEvent();
        loginInfoEvent.setTenantId(tenantId);
        loginInfoEvent.setUsername(username);
        loginInfoEvent.setStatus(status);
        loginInfoEvent.setMessage(message);
        SpringUtil.getApplicationContext().publishEvent(loginInfoEvent);
    }

    /**
     * 登录校验
     */
    public void checkLogin(LoginType loginType, String tenantId, String username, Supplier<Boolean> supplier) {
        String errorKey = Constants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;
        Integer maxRetryCount = userPasswordProperties.getMaxRetryCount();
        Duration lockTime = userPasswordProperties.getLockTime();

        // 获取用户登录错误次数，默认为0 (可自定义限制策略 例如: key + username + ip)
        int errorNumber = ObjectUtil.defaultIfNull(RedisUtils.getCacheObject(errorKey), 0);
        // 锁定时间内登录 则踢出
        if (errorNumber >= maxRetryCount) {
            recordLoginInfo(tenantId, username, loginFail, I18nUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 错误次数递增
            errorNumber++;
            RedisUtils.setCacheObject(errorKey, errorNumber, lockTime);
            // 达到规定错误次数 则锁定登录
            if (errorNumber >= maxRetryCount) {
                recordLoginInfo(tenantId, username, loginFail, I18nUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数
                recordLoginInfo(tenantId, username, loginFail, I18nUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }

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
        } else if (Boolean.FALSE.equals(tenant.getEnable())) {
            log.info("登录租户：{} 已被停用.", tenantId);
            throw new RuntimeException("tenant.blocked");
        } else if (ObjectUtil.isNotNull(tenant.getExpireTime()) && LocalDateTime.now().isAfter(tenant.getExpireTime())) {
            log.info("登录租户：{} 已超过有效期.", tenantId);
            throw new RuntimeException("tenant.expired");
        }
    }

    /**
     * 构建登录用户
     */
    public LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        Long userId = user.getId();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(userId);
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUsername(user.getUsername());
        loginUser.setNickname(user.getNickname());
        loginUser.setPassword(user.getPassword());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(sysPermissionService.getMenuPermission(userId));
        loginUser.setRolePermission(sysPermissionService.getRolePermission(userId));
        if (ObjectUtil.isNotNull(user.getDeptId())) {
            Opt<SysDeptVo> deptOpt = Opt.of(user.getDeptId()).map(sysDeptService::selectDeptById);
            loginUser.setDeptName(deptOpt.map(SysDeptVo::getDeptName).orElse(StringUtils.EMPTY));
            loginUser.setDeptCategory(deptOpt.map(SysDeptVo::getDeptCategory).orElse(StringUtils.EMPTY));
        }
        List<SysRoleVo> roles = sysRoleService.selectRolesByUserId(userId);
        List<SysPostVo> posts = sysPostService.selectPostsByUserId(userId);
        loginUser.setRoles(BeanUtil.copyToList(roles, RoleDTO.class));
        loginUser.setPosts(BeanUtil.copyToList(posts, PostDTO.class));
        return loginUser;
    }
}
