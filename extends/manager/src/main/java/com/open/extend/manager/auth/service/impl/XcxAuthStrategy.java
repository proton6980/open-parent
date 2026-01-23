package com.open.extend.manager.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
import com.open.common.spring.exception.OpenBusinessException;
import com.open.common.business.pojo.model.XcxLoginUser;
import com.open.common.core.utils.JacksonUtils;
import com.open.common.core.utils.ValidatorUtils;
import com.open.extend.manager.auth.service.IAuthStrategy;
import com.open.extend.manager.auth.service.TokenService;
import com.open.extend.manager.client.domain.vo.SysClientVo;
import com.open.extend.manager.auth.bo.XcxLoginBody;
import com.open.extend.manager.auth.vo.LoginVo;
import com.open.extend.manager.core.exception.UserException;
import com.open.extend.manager.user.domain.SysUser;
import com.open.extend.manager.user.domain.enums.UserStatus;
import com.open.extend.manager.user.service.ISysUserService;
import com.open.starter.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWechatMiniProgramRequest;
import org.springframework.stereotype.Service;

/**
 * 邮件认证策略
 *
 * @author open
 */
@Slf4j
@Service("xcx" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class XcxAuthStrategy implements IAuthStrategy {
    private ISysUserService sysUserService;

    private final TokenService tokenService;

    @Override
    public LoginVo login(String body, SysClientVo client) {
        XcxLoginBody loginBody = JacksonUtils.parseObject(body, XcxLoginBody.class);
        ValidatorUtils.validate(loginBody);
        // xcxCode 为 小程序调用 wx.login 授权后获取
        String xcxCode = loginBody.getXcxCode();
        // 多个小程序识别使用
        String appid = loginBody.getAppid();

        // 校验 appid + appsrcret + xcxCode 调用登录凭证校验接口 获取 session_key 与 openid
        AuthRequest authRequest = new AuthWechatMiniProgramRequest(AuthConfig.builder()
            .clientId(appid).clientSecret("自行填写密钥 可根据不同appid填入不同密钥")
            .ignoreCheckRedirectUri(true).ignoreCheckState(true).build());
        AuthCallback authCallback = new AuthCallback();
        authCallback.setCode(xcxCode);
        AuthResponse<AuthUser> resp = authRequest.login(authCallback);
        String openid, unionId;
        if (resp.ok()) {
            AuthToken token = resp.getData().getToken();
            openid = token.getOpenId();
            // 微信小程序只有关联到微信开放平台下之后才能获取到 unionId，因此unionId不一定能返回。
            unionId = token.getUnionId();
        } else {
            throw new OpenBusinessException(resp.getMsg());
        }
        // todo getUserInfoByOpenid 方法内部查询逻辑需要自行根据业务实现
        XcxLoginUser loginUser = this.getUserInfoByOpenid(openid);
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
        loginVo.setOpenid(openid);
        return loginVo;
    }

    private XcxLoginUser getUserInfoByOpenid(String openid) throws UserException {
        // todo 自行实现 userService.selectUserByOpenid(openid);
        SysUser user = new SysUser();
        if (ObjectUtil.isNull(user)) {
            // todo 用户不存在 业务逻辑自行实现
        }
        if (!UserStatus.NORMAL.equals(user.getStatus())) {
            // todo 用户已被停用 业务逻辑自行实现
        }
        // 框架登录不限制从什么表查询 只要最终构建出 LoginUser 即可
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        XcxLoginUser loginUser = new XcxLoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setNickname(user.getNickname());
        loginUser.setUserType(user.getUserType());
        loginUser.setOpenid(openid);
        return loginUser;
    }
}
