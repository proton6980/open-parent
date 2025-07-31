package com.open.starter.mybatisplus.config;

import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.PostInitTableInfoHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.open.starter.mybatisplus.aspect.DataPermissionAspect;
import com.open.starter.mybatisplus.core.convert.Base64ImageConvertor;
import com.open.starter.mybatisplus.handler.InjectionMetaObjectHandler;
import com.open.starter.mybatisplus.handler.OpenMybatisPlusExceptionHandler;
import com.open.starter.mybatisplus.handler.OpenPostInitTableInfoHandler;
import com.open.starter.mybatisplus.interceptor.OpenDataPermissionInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus 自动化配置
 * <p
 * <a href="https://baomidou.com/pages/97710a/">PaginationInnerInterceptor 分页插件，自动识别数据库类型</a><br>
 * <a href="https://baomidou.com/pages/0d93c0/">OptimisticLockerInnerInterceptor 乐观锁插件</a><br>
 * <a href="https://baomidou.com/pages/4c6bcf/">MetaObjectHandler 元对象字段填充控制器</a><br>
 * <a href="https://baomidou.com/pages/42ea4a/">ISqlInjector sql注入器</a><br>
 * <a href="https://baomidou.com/pages/f9a237/">BlockAttackInnerInterceptor 如果是对全表的删除或更新操作，就会终止该操作</a><br>
 * <a href="">IllegalSQLInnerInterceptor sql性能规范插件(垃圾SQL拦截)</a><br>
 * <a href="https://baomidou.com/pages/568eb2/">IdentifierGenerator 自定义主键策略</a><br>
 * <a href="https://baomidou.com/pages/aef2f2/">TenantLineInnerInterceptor 多租户插件</a><br>
 * <a href="https://baomidou.com/pages/2a45ff/">DynamicTableNameInnerInterceptor 动态表名插件</a><br>
 * </p
 *
 * @author open
 */
@MapperScan(basePackages = "${mybatis-plus.global-config.mapper-scan:com.open.**.mapper}")
@Configuration
public class OpenMybatisPlusAutoConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户插件 必须放到第一位
        try {
            TenantLineInnerInterceptor tenant = SpringUtil.getBean(TenantLineInnerInterceptor.class);
            interceptor.addInnerInterceptor(tenant);
        } catch (BeansException ignore) {
        }
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 数据权限拦截器
     */
    public OpenDataPermissionInterceptor dataPermissionInterceptor() {
        return new OpenDataPermissionInterceptor(SpringUtil.getProperty("mybatis-plus.global-config.mapper-scan"));
    }

    /**
     * 数据权限切面处理器
     */
    @Bean
    public DataPermissionAspect dataPermissionAspect() {
        return new DataPermissionAspect();
    }

    /**
     * 分页插件，自动识别数据库类型
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 分页合理化
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁插件
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 元对象字段填充控制器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new InjectionMetaObjectHandler();
    }

    /**
     * 使用网卡信息绑定雪花生成器
     * 防止集群雪花ID重复
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(NetUtil.getLocalhost());
    }

    /**
     * 异常处理器
     */
    @Bean
    public OpenMybatisPlusExceptionHandler mybatisExceptionHandler() {
        return new OpenMybatisPlusExceptionHandler();
    }

    /**
     * 初始化表对象处理器
     */
    @Bean
    public PostInitTableInfoHandler postInitTableInfoHandler() {
        return new OpenPostInitTableInfoHandler();
    }

    /**
     * base64图片转换器
     */
    @Bean
    public Base64ImageConvertor base64ImageConvertor() {
        return new Base64ImageConvertor();
    }
}
