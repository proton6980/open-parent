package com.open.extend.i18n.config;

import com.open.extend.i18n.properties.I18nProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * I18n自动化配置
 *
 * @author godLian
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(I18nProperties.class)
public class OpenI18nAutoConfiguration {

    @Bean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnProperty(prefix = "open.i18n", name = "init-sql", havingValue = "true")
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource,
                                                       ResourceLoader resourceLoader) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(
                resourceLoader.getResource("classpath:i18n-db/schema.sql")
        ));
        return initializer;
    }
}
