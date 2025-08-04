package com.open.starter.satoken.config;

import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpLogic;
import com.open.commons.factory.YmlPropertySourceFactory;
import com.open.starter.satoken.aspect.RepeatSubmitAspect;
import com.open.starter.satoken.core.dao.OpenSaTokenDao;
import com.open.starter.satoken.core.service.SaPermissionImpl;
import com.open.starter.satoken.exception.OpenSaTokenExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Sa-Token自动化配置
 *
 * @author open
 */
@Configuration
@PropertySource(value = "classpath:common-satoken.yml", factory = YmlPropertySourceFactory.class)
public class OpenSaTokenAutoConfiguration {

//    @Bean
//    public SaTokenContext saTokenContext() {
//        return new OpenSaTokenContext();
//    }

    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    public StpInterface stpInterface() {
        return new SaPermissionImpl();
    }

    /**
     * 自定义dao层存储
     */
    @Bean
    public SaTokenDao saTokenDao() {
        return new OpenSaTokenDao();
    }

    /**
     * 异常处理器
     */
    @Bean
    public OpenSaTokenExceptionHandler saTokenExceptionHandler() {
        return new OpenSaTokenExceptionHandler();
    }

    /**
     * 重复提交切面
     */
    @Bean
    public RepeatSubmitAspect repeatSubmitAspect() {
        return new RepeatSubmitAspect();
    }

}

