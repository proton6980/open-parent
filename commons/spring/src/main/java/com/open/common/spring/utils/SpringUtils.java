package com.open.common.spring.utils;

import cn.hutool.extra.spring.SpringUtil;

import java.util.Map;

/**
 * Spring(Spring boot)工具封装，包括：
 * Spring IOC容器中的bean对象获取
 * 注册和注销Bean
 *
 * @author open
 */
public class SpringUtils extends SpringUtil {

    /**
     * 判断bean是否存在
     *
     * @param beanName bean名称
     * @return 是否存在
     */
    public static boolean containsBean(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }

    /**
     * 判断bean是否存在
     *
     * @param clazz bean类
     * @return 是否存在
     */
    public static <T> boolean containsBean(Class<T> clazz) {
        Map<String, T> beans = getBeansOfType(clazz);
        return !beans.isEmpty();
    }

}
