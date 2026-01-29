package com.open.extend.manager.function.menu.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型
 *
 * @author open
 */
@Getter
@AllArgsConstructor
public enum MenuType {
    DIRECTORY("M", "目录"),
    MENU("C", "菜单"),
    BUTTON("F", "按钮"),
    ;
    @EnumValue
    private final String key;

    private final String value;
}
