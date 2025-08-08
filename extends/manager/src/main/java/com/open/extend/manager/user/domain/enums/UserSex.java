package com.open.extend.manager.user.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户性别
 * @author open
 */
@Getter
@AllArgsConstructor
public enum UserSex {
    MALE(0, "男"),
    FEMALE(1, "女"),
    UNKNOWN(2, "未知"),
    ;

    @EnumValue
    private final Integer key;

    private final String value;
}
