package com.carsonlius.module.system.enums.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @version V1.0
 * @author: carsonlius
 * @date: 2023/12/22 15:23
 * @company
 * @description 菜单类型枚举类
 */

@AllArgsConstructor
@Getter
public enum MenuTypeEnum {
    DIR(1), // 目录
    MENU(2), // 菜单
    BUTTON(3) // 按钮
    ;

    /**
     * 类型
     */
    private final Integer type;
}
