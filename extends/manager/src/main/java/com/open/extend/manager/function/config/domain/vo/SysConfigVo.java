package com.open.extend.manager.function.config.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.open.extend.manager.function.config.domain.SysConfig;
import com.open.starter.excel.annotation.ExcelDictFormat;
import com.open.starter.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 参数配置视图对象 sys_config
 *
 * @author open
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysConfig.class)
public class SysConfigVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @ExcelProperty(value = "参数主键")
    private Long configId;

    /**
     * 参数名称
     */
    @ExcelProperty(value = "参数名称")
    private String configName;

    /**
     * 参数键名
     */
    @ExcelProperty(value = "参数键名")
    private String configKey;

    /**
     * 参数键值
     */
    @ExcelProperty(value = "参数键值")
    private String configValue;

    /**
     * 系统内置|否
     */
    @ExcelProperty(value = "系统内置", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_yes_no")
    private Boolean isInner;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
