package com.open.starter.mybatisplus.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.handlers.PostInitTableInfoHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.open.commons.utils.ReflectUtils;
import org.apache.ibatis.session.Configuration;

/**
 * 修改表信息初始化方式
 * 目前用于全局修改是否使用逻辑删除
 *
 * @author open
 */
public class OpenPostInitTableInfoHandler implements PostInitTableInfoHandler {

    @Override
    public void postTableInfo(TableInfo tableInfo, Configuration configuration) {
        String flag = SpringUtil.getProperty("mybatis-plus.enableLogicDelete", "true");
        // 只有关闭时 统一设置false 为true时mp自动判断不处理
        if (!Convert.toBool(flag)) {
            ReflectUtils.setFieldValue(tableInfo, "withLogicDelete", false);
        }
    }

}

