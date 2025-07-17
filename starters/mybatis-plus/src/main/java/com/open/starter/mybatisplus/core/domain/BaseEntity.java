package com.open.starter.mybatisplus.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 *
 * @author godLian
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     * <p>未删除 = 0</p>
     * <p>删除 = 执行删除时的时间戳</p>
     */
    @TableLogic(value = "0", delval = "UNIX_TIMESTAMP * 1000")
    @TableField(fill = FieldFill.INSERT)
    private long deleted;
}
