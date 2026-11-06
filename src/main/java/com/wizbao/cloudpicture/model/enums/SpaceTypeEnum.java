package com.wizbao.cloudpicture.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum SpaceTypeEnum {

    PRIVATE("私有空间", 0),
    TEAM("团队空间", 1);

    /**
     * 空间类型名称
     */
    private final String text;

    /**
     * 空间类型值
     */
    private final int value;

    /**
     * 构造空间类型
     *
     * @param text 空间类型名称
     * @param value 空间类型值
     */
    SpaceTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static SpaceTypeEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (SpaceTypeEnum spaceTypeEnum : SpaceTypeEnum.values()) {
            if (spaceTypeEnum.value == value) {
                return spaceTypeEnum;
            }
        }
        return null;
    }
}
