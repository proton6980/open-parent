package com.open.extend.manager.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.open.commons.exception.BusinessException;
import com.open.commons.pojo.model.LoginUser;
import com.open.commons.utils.JacksonUtils;
import com.open.commons.utils.StreamUtils;
import com.open.commons.utils.ValidatorUtils;
import com.open.extend.manager.auth.service.IAuthStrategy;
import com.open.extend.manager.auth.service.TokenService;
import com.open.extend.manager.client.domain.vo.SysClientVo;
import com.open.extend.manager.auth.bo.SocialLoginBody;
import com.open.extend.manager.auth.vo.LoginVo;
import com.open.extend.manager.domain.vo.SysSocialVo;
import com.open.extend.manager.exception.UserException;
import com.open.extend.manager.service.ISysSocialService;
import com.open.extend.manager.user.domain.SysUser;
import com.open.extend.manager.user.service.ISysUserService;
import com.open.starter.satoken.utils.LoginHelper;
import com.open.starter.social.properties.SocialProperties;
import com.open.starter.social.utils.SocialUtils;
import com.open.starter.tenant.utils.TenantHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 第三方授权策略
 *
 * @author thiszhc is 三三
 */
@Slf4j
@Service("social" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class SocialAuthStrategy implements IAuthStrategy {
    private final TokenService tokenService;
    private ISysSocialService sysSocialService;
    private final ISysUserService sysUserService;

    private final SocialProperties socialProperties;

    /**
     * 登录-第三方授权登录
     *
     * @param body     登录信息
     * @param client   客户端信息
     */
    @Override
    public LoginVo login(String body, SysClientVo client) {
        SocialLoginBody loginBody = JacksonUtils.parseObject(body, SocialLoginBody.class);
        ValidatorUtils.validate(loginBody);
        AuthResponse<AuthUser> response = SocialUtils.loginAuth(
            loginBody.getSource(), loginBody.getSocialCode(),
            loginBody.getSocialState(), socialProperties);
        if (!response.ok()) {
            throw new BusinessException(response.getMsg());
        }
        AuthUser authUserData = response.getData();
        if ("GITEE".equals(authUserData.getSource())) {
            // 如用户使用 gitee 登录顺手 star 给作者一点支持 拒绝白嫖
            HttpUtil.createRequest(Method.PUT, "https://gitee.com/api/v5/user/starred/dromara/RuoYi-Vue-Plus")
                .formStr(MapUtil.of("access_token", authUserData.getToken().getAccessToken()))
                .executeAsync();
            HttpUtil.createRequest(Method.PUT, "https://gitee.com/api/v5/user/starred/dromara/RuoYi-Cloud-Plus")
                .formStr(MapUtil.of("access_token", authUserData.getToken().getAccessToken()))
                .executeAsync();
        }

        List<SysSocialVo> list = sysSocialService.selectByAuthId(authUserData.getSource() + authUserData.getUuid());
        if (CollUtil.isEmpty(list)) {
            throw new BusinessException("你还没有绑定第三方账号，绑定后才可以登录！");
        }
        SysSocialVo socialVo;
        if (TenantHelper.isEnable()) {
            Optional<SysSocialVo> opt = StreamUtils.findAny(list, x -> x.getTenantId().equals(loginBody.getTenantId()));
            if (opt.isPresent()) {
                throw new BusinessException("对不起，你没有权限登录当前租户！");
            }
            socialVo = opt.get();
        } else {
            socialVo = list.get(0);
        }

        LoginUser loginUser = this.getUserInfo(socialVo.getUserId(), socialVo.getTenantId());
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

    public LoginUser getUserInfo(Long userId, String tenantId) throws UserException {
        return TenantHelper.dynamic(tenantId, () -> {
            SysUser sysUser = sysUserService.getById(userId);
            if (ObjectUtil.isNull(sysUser)) {
                throw new UserException("user.not.exists", "");
            }
            if (!sysUser.getEnable()) {
                throw new UserException("user.blocked", sysUser.getUsername());
            }
            // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
            // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
            return tokenService.buildLoginUser(sysUser);
        });
    }
}
