package com.wizbao.cloudpicture.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum PictureReviewStatusEnum {  
    REVIEWING("待审核", 0),  
    PASS("通过", 1),  
    REJECT("拒绝", 2);  
  
    private final String text;  
    private final int value;  
  
    /**
     * @param text  审核状态名称
     * @param value 审核状态值
     */
    PictureReviewStatusEnum(String text, int value) {  
        this.text = text;  
        this.value = value;  
    }  
  
    /**  
     * 根据 value 获取枚举  
     */  
    public static PictureReviewStatusEnum getEnumByValue(Integer value) {  
        if (ObjUtil.isEmpty(value)) {  
            return null;  
        }  
        for (PictureReviewStatusEnum pictureReviewStatusEnum : PictureReviewStatusEnum.values()) {  
            if (pictureReviewStatusEnum.value == value) {  
                return pictureReviewStatusEnum;  
            }  
        }  
        return null;  
    }  
}
