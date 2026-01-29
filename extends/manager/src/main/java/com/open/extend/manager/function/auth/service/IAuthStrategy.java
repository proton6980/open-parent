package com.open.extend.manager.function.auth.service;

import com.open.common.spring.exception.OpenBusinessException;
import com.open.common.spring.utils.SpringUtils;
import com.open.extend.manager.function.auth.vo.LoginVo;
import com.open.extend.manager.function.client.domain.vo.SysClientVo;

/**
 * 授权策略
 *
 * @author open
 */
public interface IAuthStrategy {

    String BASE_NAME = "AuthStrategy";

    /**
     * 登录
     *
     * @param body      登录对象
     * @param client    授权管理视图对象
     * @param grantType 授权类型
     * @return 登录验证信息
     */
    static LoginVo login(String body, SysClientVo client, String grantType) {
        // 授权类型和客户端id
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.getBeanFactory().containsBean(beanName)) {
            throw new OpenBusinessException("授权类型不正确!");
        }
        IAuthStrategy instance = SpringUtils.getBean(beanName);
        return instance.login(body, client);
    }

    /**
     * 登录
     *
     * @param body   登录对象
     * @param client 授权管理视图对象
     * @return 登录验证信息
     */
    LoginVo login(String body, SysClientVo client);

}
