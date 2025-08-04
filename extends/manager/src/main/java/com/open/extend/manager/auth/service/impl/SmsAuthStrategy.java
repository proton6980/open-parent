package com.open.extend.manager.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.open.commons.constants.Constants;
import com.open.commons.constants.GlobalConstants;
import com.open.commons.enums.LoginType;
import com.open.commons.pojo.model.LoginUser;
import com.open.commons.utils.JacksonUtils;
import com.open.commons.utils.MessageUtils;
import com.open.commons.utils.StringUtils;
import com.open.commons.utils.ValidatorUtils;
import com.open.extend.manager.auth.service.IAuthStrategy;
import com.open.extend.manager.auth.service.TokenService;
import com.open.extend.manager.client.domain.vo.SysClientVo;
import com.open.extend.manager.auth.bo.SmsLoginBody;
import com.open.extend.manager.auth.vo.LoginVo;
import com.open.extend.manager.exception.CaptchaExpireException;
import com.open.extend.manager.exception.UserException;
import com.open.extend.manager.user.domain.SysUser;
import com.open.extend.manager.user.service.ISysUserService;
import com.open.starter.cache.utils.RedisUtils;
import com.open.starter.satoken.utils.LoginHelper;
import com.open.starter.tenant.utils.TenantHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 短信认证策略
 *
 * @author Michelle.Chung
 */
@Slf4j
@Service("sms" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class SmsAuthStrategy implements IAuthStrategy {
    private final ISysUserService sysUserService;

    private final TokenService tokenService;

    @Override
    public LoginVo login(String body, SysClientVo client) {
        SmsLoginBody loginBody = JacksonUtils.parseObject(body, SmsLoginBody.class);
        ValidatorUtils.validate(loginBody);
        String tenantId = loginBody.getTenantId();
        String mobile = loginBody.getMobilePrefix() + " " + loginBody.getMobileSuffix();
        String smsCode = loginBody.getSmsCode();
        LoginUser loginUser = TenantHelper.dynamic(tenantId, () -> {
            LoginUser user = this.getUserInfoByMobile(loginBody.getMobilePrefix(), loginBody.getMobileSuffix(), tenantId);
            tokenService.checkLogin(LoginType.SMS, tenantId, user.getUsername(),
                    () -> !validateSmsCode(tenantId, mobile, smsCode));
            return user;
        });
        loginUser.setClientKey(client.getClientKey());
        loginUser.setDeviceType(client.getDeviceType());
        SaLoginParameter model = new SaLoginParameter();
        model.setDeviceType(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
        // 生成token
        LoginHelper.login(loginUser, model);

        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        return loginVo;
    }

    private LoginUser getUserInfoByMobile(String mobilePrefix, String mobileSuffix, String tenantId) throws UserException {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getMobilePrefix, mobilePrefix)
                    .eq(SysUser::getMobileSuffix, mobileSuffix));
            if (ObjectUtil.isNull(sysUser)) {
                throw new UserException("user.not.exists", mobilePrefix + " " + mobileSuffix);
            }
            if (!sysUser.getEnable()) {
                throw new UserException("user.blocked", mobilePrefix + " " + mobileSuffix);
            }
            // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
            // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
            return tokenService.buildLoginUser(sysUser);
        });
    }

    /**
     * 校验短信验证码
     */
    private boolean validateSmsCode(String tenantId, String phonenumber, String smsCode) {
        String code = RedisUtils.getCacheObject(GlobalConstants.CAPTCHA_CODE_KEY + phonenumber);
        if (StringUtils.isBlank(code)) {
            tokenService.recordLogininfor(tenantId, phonenumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        return code.equals(smsCode);
    }

}
