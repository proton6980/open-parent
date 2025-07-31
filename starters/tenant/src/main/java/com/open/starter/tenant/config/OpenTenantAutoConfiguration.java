package com.open.starter.tenant.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.open.commons.utils.ReflectUtils;
import com.open.starter.cache.config.OpenRedisAutoConfiguration;
import com.open.starter.cache.properties.RedissonProperties;
import com.open.starter.tenant.core.dao.TenantSaTokenDao;
import com.open.starter.tenant.handler.OpenTenantLineHandler;
import com.open.starter.tenant.handler.TenantKeyPrefixHandler;
import com.open.starter.tenant.manager.TenantSpringCacheManager;
import com.open.starter.tenant.properties.TenantProperties;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 租户配置类
 *
 * @author open
 */
@EnableConfigurationProperties(TenantProperties.class)
@ImportAutoConfiguration(OpenRedisAutoConfiguration.class)
@Configuration
@ConditionalOnProperty(value = "open.tenant.enable", havingValue = "true")
public class OpenTenantAutoConfiguration {

    @ConditionalOnClass(TenantLineInnerInterceptor.class)
    @Configuration
    static class MybatisPlusConfig {

        /**
         * 多租户插件
         */
        @Bean
        public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties tenantProperties) {
            return new TenantLineInnerInterceptor(new OpenTenantLineHandler(tenantProperties));
        }
    }

    @Bean
    public RedissonAutoConfigurationCustomizer tenantRedissonCustomizer(RedissonProperties redissonProperties) {
        return config -> {
            TenantKeyPrefixHandler nameMapper = new TenantKeyPrefixHandler(redissonProperties.getKeyPrefix());
            SingleServerConfig singleServerConfig = ReflectUtils.invokeGetter(config, "singleServerConfig");
            if (ObjectUtil.isNotNull(singleServerConfig)) {
                // 使用单机模式
                // 设置多租户 redis key前缀
                singleServerConfig.setNameMapper(nameMapper);
            }
            ClusterServersConfig clusterServersConfig = ReflectUtils.invokeGetter(config, "clusterServersConfig");
            // 集群配置方式 参考下方注释
            if (ObjectUtil.isNotNull(clusterServersConfig)) {
                // 设置多租户 redis key前缀
                clusterServersConfig.setNameMapper(nameMapper);
            }
        };
    }

    /**
     * 多租户缓存管理器
     */
    @Primary
    @Bean
    public CacheManager tenantCacheManager() {
        return new TenantSpringCacheManager();
    }

    /**
     * 多租户鉴权dao实现
     */
    @Primary
    @Bean
    public SaTokenDao tenantSaTokenDao() {
        return new TenantSaTokenDao();
    }

}

