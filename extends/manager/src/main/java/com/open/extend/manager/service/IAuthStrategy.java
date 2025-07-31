package com.open.extend.manager.service;

import cn.hutool.extra.spring.SpringUtil;
import com.open.commons.exception.BaseException;
import com.open.extend.manager.controller.vo.LoginVo;
import com.open.extend.manager.domain.vo.SysClientVo;

/**
 * 授权策略
 *
 * @author Michelle.Chung
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
        if (!SpringUtil.getBeanFactory().containsBean(beanName)) {
            throw new BaseException("授权类型不正确!");
        }
        IAuthStrategy instance = SpringUtil.getBean(beanName);
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
