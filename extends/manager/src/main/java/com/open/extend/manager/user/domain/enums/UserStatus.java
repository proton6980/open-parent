package com.open.extend.manager.user.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author open
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    NORMAL(0, "正常"),
    DISABLE(1, "停用"),
    ;

    @EnumValue
    private final Integer key;

    private final String value;
}
